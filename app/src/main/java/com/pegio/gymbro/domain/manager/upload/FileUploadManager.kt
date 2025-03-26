package com.pegio.gymbro.domain.manager.upload

import android.net.Uri
import java.util.UUID

interface FileUploadManager {
    fun enqueueFileUpload(uri: Uri, fileType: FileType): UUID
    fun deleteFile(url: String)

    companion object {
        const val URI_KEY = "image_uri"
        const val FILE_TYPE_KEY = "file_type"
        const val RESULT_URL = "result_url"
    }
}