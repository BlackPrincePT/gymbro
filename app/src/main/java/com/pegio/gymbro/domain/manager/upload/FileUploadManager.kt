package com.pegio.gymbro.domain.manager.upload

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource

interface FileUploadManager {
    suspend fun enqueueFileUpload(uri: String): Resource<String, DataError.Firestore>
    fun deleteFile(url: String)

    companion object {
        const val URI_KEY = "content_uri"
        const val RESULT_URL = "result_url"
    }
}