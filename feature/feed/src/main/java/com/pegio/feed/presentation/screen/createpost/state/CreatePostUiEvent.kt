package com.pegio.feed.presentation.screen.createpost.state

import android.net.Uri

sealed interface CreatePostUiEvent {

    // Main
    data class OnPostTextChange(val value: String) : CreatePostUiEvent

    // Top Bar
    data object OnPostClick : CreatePostUiEvent
    data object OnCancelClick : CreatePostUiEvent

    // Compose State
    data class OnPhotoSelected(val imageUri: Uri) : CreatePostUiEvent
}