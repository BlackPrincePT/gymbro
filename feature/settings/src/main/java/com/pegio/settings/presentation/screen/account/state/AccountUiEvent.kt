package com.pegio.settings.presentation.screen.account.state

import android.net.Uri
import com.pegio.model.User.Gender

sealed interface AccountUiEvent {

    // Main
    data object OnLaunchGallery : AccountUiEvent
    data class OnPhotoSelected(val imageUri: Uri) : AccountUiEvent

    // Form
    data object OnUsernameSubmit : AccountUiEvent
    data object OnAgeSubmit : AccountUiEvent
    data object OnGenderSubmit : AccountUiEvent
    data object OnHeightSubmit : AccountUiEvent
    data object OnWeightSubmit : AccountUiEvent

    // Navigation
    data object OnBackClick : AccountUiEvent

    // Compose State
    data class OnUsernameChanged(val username: String) : AccountUiEvent
    data class OnAgeChanged(val age: String) : AccountUiEvent
    data class OnGenderChanged(val gender: Gender) : AccountUiEvent
    data class OnHeightChanged(val height: String) : AccountUiEvent
    data class OnWeightChanged(val weight: String) : AccountUiEvent
    data class OnGenderMenuExpandedChange(val isExpanded: Boolean) : AccountUiEvent
}