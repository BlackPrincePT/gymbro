package com.pegio.auth.presentation.screen.register.state

import android.net.Uri
import com.pegio.model.User.Gender

sealed interface RegisterUiEvent {

    // Main
    data object OnSubmit : RegisterUiEvent
    data object OnLaunchGallery : RegisterUiEvent

    // Bottom Sheet
    data class OnBottomSheetStateUpdate(val shouldShow: Boolean) : RegisterUiEvent

    // Compose State
    data class OnUsernameChanged(val username: String) : RegisterUiEvent
    data class OnAgeChanged(val age: String) : RegisterUiEvent
    data class OnGenderChanged(val gender: Gender) : RegisterUiEvent
    data class OnHeightChanged(val height: String) : RegisterUiEvent
    data class OnWeightChanged(val weight: String) : RegisterUiEvent
    data class OnProfilePhotoSelected(val imageUri: Uri?) : RegisterUiEvent
    data class OnGenderMenuExpandedChange(val isExpanded: Boolean) : RegisterUiEvent
}