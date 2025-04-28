package com.pegio.feed.presentation.screen.createpost.state

import android.net.Uri

sealed interface CreatePostUiEvent {

    // Main
    data object OnOpenGallery : CreatePostUiEvent
    data object OnPostClick : CreatePostUiEvent
    data class OnSelectedWorkoutUpdate(val id: String?) : CreatePostUiEvent

    // Navigation
    data object OnCancelClick : CreatePostUiEvent
    data object OnChooseWorkoutClick : CreatePostUiEvent

    // Compose State
    data class OnPhotoSelected(val imageUri: Uri) : CreatePostUiEvent
    data class OnPostTextChange(val value: String) : CreatePostUiEvent
}