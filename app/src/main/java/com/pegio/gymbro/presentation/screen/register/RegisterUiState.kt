package com.pegio.gymbro.presentation.screen.register

import android.net.Uri
import com.pegio.gymbro.presentation.model.UiUser

data class RegisterUiState(
    val user: UiUser = UiUser.EMPTY,
    val selectedImageUri: Uri? = null
)