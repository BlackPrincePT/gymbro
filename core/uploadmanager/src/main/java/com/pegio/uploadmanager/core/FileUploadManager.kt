package com.pegio.uploadmanager.core

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource

interface FileUploadManager {
    suspend fun enqueueFileUpload(uri: String): Resource<String, DataError.Firestore>
    fun deleteFile(url: String)
}