package com.pegio.presentation.screen

import android.net.Uri

sealed interface AccountUiEvent {
    data class OnPhotoSelected(val imageUri: Uri) : AccountUiEvent
    data class OnPhotoUpload(val imageUrl: String) : AccountUiEvent

    // Top Bar
    data object OnBackClick : AccountUiEvent
}