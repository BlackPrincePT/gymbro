package com.pegio.gymbro.presentation.screen.register

import android.net.Uri
import com.pegio.gymbro.domain.model.User

sealed interface RegisterUiEvent {
    data class OnUsernameChanged(val username: String) : RegisterUiEvent
    data class OnAgeChanged(val age: String) : RegisterUiEvent
    data class OnGenderChanged(val gender: User.Gender) : RegisterUiEvent
    data class OnHeightChanged(val height: String) : RegisterUiEvent
    data class OnWeightChanged(val weight: String) : RegisterUiEvent
    data class OnProfilePhotoSelected(val imageUri: Uri) : RegisterUiEvent
    data object OnSubmit : RegisterUiEvent
}