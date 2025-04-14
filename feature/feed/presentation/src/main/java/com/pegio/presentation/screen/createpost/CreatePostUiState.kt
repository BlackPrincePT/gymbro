package com.pegio.presentation.screen.createpost

import android.net.Uri

data class CreatePostUiState(
    val postText: String = "",
    val imageUri: Uri? = null
)