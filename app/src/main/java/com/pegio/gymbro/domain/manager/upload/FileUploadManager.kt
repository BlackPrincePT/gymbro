package com.pegio.gymbro.domain.manager.upload

import android.net.Uri
import java.util.UUID

interface FileUploadManager {
    fun enqueueFileUpload(uri: Uri, fileType: FileType): UUID
    fun deleteFile(url: String)
}