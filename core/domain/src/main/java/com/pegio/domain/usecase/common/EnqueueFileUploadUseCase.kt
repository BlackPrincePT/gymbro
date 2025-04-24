package com.pegio.domain.usecase.common

import com.pegio.uploadmanager.core.FileUploadManager
import javax.inject.Inject

class EnqueueFileUploadUseCase @Inject constructor(private val uploadManager: FileUploadManager) {
    suspend operator fun invoke(uri: String) = uploadManager.enqueueFileUpload(uri)
}