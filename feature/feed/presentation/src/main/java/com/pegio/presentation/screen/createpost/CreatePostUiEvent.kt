package com.pegio.presentation.screen.createpost

import android.net.Uri

sealed interface CreatePostUiEvent {
    data class OnPostTextChange(val value: String) : CreatePostUiEvent
    data class OnPhotoSelected(val imageUri: Uri) : CreatePostUiEvent
    data object OnPostClick : CreatePostUiEvent
    data object OnCancelClick : CreatePostUiEvent
}