package com.pegio.domain.usecase.common

import com.pegio.uploadmanager.core.FileUploadManager
import javax.inject.Inject

class DeleteFileUseCase @Inject constructor(private val uploadManager: FileUploadManager) {
    operator fun invoke(url: String) = uploadManager.deleteFile(url)
}