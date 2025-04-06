package com.pegio.gymbro.presentation.screen.account

import android.net.Uri

sealed interface AccountUiEvent {
    data class OnPhotoSelected(val imageUri: Uri) : AccountUiEvent
    data class OnPhotoUpload(val imageUrl: String) : AccountUiEvent

    // Top Bar
    data object OnBackClick : AccountUiEvent
}