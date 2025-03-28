package com.pegio.gymbro.data.workmanager

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.pegio.gymbro.domain.manager.upload.FileType
import com.pegio.gymbro.domain.manager.upload.FileUploadManager.Companion.FILE_TYPE_KEY
import com.pegio.gymbro.domain.manager.upload.FileUploadManager.Companion.RESULT_URL
import com.pegio.gymbro.domain.manager.upload.FileUploadManager.Companion.URI_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import java.util.UUID

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val storageRef = FirebaseStorage.getInstance().reference

    override suspend fun doWork(): Result {
        val uriString = inputData.getString(URI_KEY)
        val fileTypeString = inputData.getString(FILE_TYPE_KEY) ?: return Result.failure()

        if (uriString.isNullOrEmpty())
            return Result.failure()

        return try {
            val uri = Uri.parse(uriString)
            val fileExtension = FileType.valueOf(fileTypeString).extension

            val uniquePath = UUID.randomUUID().toString() + fileExtension

            storageRef.child(uniquePath).putFile(uri).await()

            val resultUrl = storageRef.child(uniquePath).downloadUrl.await().toString()

            Result.success(workDataOf(RESULT_URL to resultUrl))
        } catch (e: Exception) {
            when (e) { // TODO - Better Error Handling
                is IllegalArgumentException -> Result.failure()
                is StorageException -> Result.failure()
                else -> Result.failure()
            }
        }
    }
}