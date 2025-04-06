package com.pegio.gymbro.presentation.screen.createpost

import android.net.Uri

data class CreatePostUiState(
    val postText: String = "",
    val imageUri: Uri? = null
)