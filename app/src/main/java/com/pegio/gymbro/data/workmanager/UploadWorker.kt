package com.pegio.gymbro.data.workmanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.storage.FirebaseStorage
import com.pegio.gymbro.domain.manager.upload.FileUploadManager.Companion.RESULT_URL
import com.pegio.gymbro.domain.manager.upload.FileUploadManager.Companion.URI_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.math.roundToInt

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val storageRef = FirebaseStorage.getInstance().reference

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
        private const val DEFAULT_THRESHOLD = 200 * 1024L
    }

    override suspend fun doWork(): Result {
        val uriString = inputData.getString(URI_KEY)

        if (uriString.isNullOrEmpty())
            return Result.failure()

        return try {
            val originalUri = Uri.parse(uriString) ?: return Result.failure()

            val mimeType = applicationContext.contentResolver.getType(originalUri)
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)

            val fileRef = storageRef.child(UUID.randomUUID().toString() + extension)

            compressImage(originalUri, DEFAULT_THRESHOLD)?.let { byteArray ->
                fileRef.putBytes(byteArray).await()
            } ?: fileRef.putFile(originalUri).await()

            val resultUrl = fileRef.downloadUrl.await().toString()

            Result.success(workDataOf(RESULT_URL to resultUrl))
        } catch (e: Exception) {
            if (runAttemptCount < MAX_RETRY_ATTEMPTS) Result.retry()
            else Result.failure()
        }
    }

    private suspend fun compressImage(contentUri: Uri, compressionThreshold: Long): ByteArray? {
        return withContext(Dispatchers.IO) outer@{
            val mimeType = applicationContext.contentResolver.getType(contentUri)
            val inputBytes = applicationContext.contentResolver
                .openInputStream(contentUri)?.use { inputStream ->
                    inputStream.readBytes()
                } ?: return@outer null

            if (inputBytes.size <= compressionThreshold)
                return@outer inputBytes

            ensureActive()

            withContext(Dispatchers.Default) {
                val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)
                    ?: return@withContext null

                ensureActive()

                val compressFormat = when (mimeType) {
                    "image/png" -> Bitmap.CompressFormat.PNG
                    "image/jpeg" -> Bitmap.CompressFormat.JPEG
                    "image/webp" -> if (Build.VERSION.SDK_INT >= 30) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.WEBP
                    else -> Bitmap.CompressFormat.JPEG
                }

                var outputBytes: ByteArray
                var quality = 90

                do {
                    ByteArrayOutputStream().use { outputStream ->
                        bitmap.compress(compressFormat, quality, outputStream)
                        outputBytes = outputStream.toByteArray()
                        quality -= (quality * 0.1).roundToInt()
                    }
                } while (isActive && outputBytes.size > compressionThreshold && quality > 5 && compressFormat != Bitmap.CompressFormat.PNG)

                outputBytes
            }
        }
    }
}