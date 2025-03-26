package com.pegio.gymbro.data.workmanager

import android.net.Uri
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.storage.FirebaseStorage
import com.pegio.gymbro.data.workmanager.UploadWorker.Companion.FILE_TYPE_KEY
import com.pegio.gymbro.data.workmanager.UploadWorker.Companion.URI_KEY
import com.pegio.gymbro.domain.manager.upload.FileType
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import java.util.UUID
import javax.inject.Inject

class UploadManager @Inject constructor(private val workManager: WorkManager) : FileUploadManager {

    private val storage = FirebaseStorage.getInstance()

    override fun enqueueFileUpload(uri: Uri, fileType: FileType): UUID {
        val inputData = workDataOf(
            URI_KEY to uri.toString(),
            FILE_TYPE_KEY to fileType.name
        )

        val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(inputData)
            .build()

        workManager.enqueue(workRequest)

        return workRequest.id
    }

    override fun deleteFile(url: String) {
        storage.getReferenceFromUrl(url).delete()
    }
}