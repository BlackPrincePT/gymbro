package com.pegio.gymbro.data.workmanager

import android.content.Context
import android.net.Uri
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.storage.FirebaseStorage
import com.pegio.gymbro.domain.manager.upload.FileType
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import com.pegio.gymbro.domain.manager.upload.FileUploadManager.Companion.FILE_TYPE_KEY
import com.pegio.gymbro.domain.manager.upload.FileUploadManager.Companion.URI_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class UploadManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) : FileUploadManager {

    private val storage = FirebaseStorage.getInstance()

    override fun enqueueFileUpload(uri: Uri, fileType: FileType): UUID {
        val inputData = workDataOf(
            URI_KEY to uri.toString(),
            FILE_TYPE_KEY to fileType.name
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(appContext).enqueue(workRequest)

        return workRequest.id
    }

    override fun deleteFile(url: String) {
        storage.getReferenceFromUrl(url).delete()
    }
}