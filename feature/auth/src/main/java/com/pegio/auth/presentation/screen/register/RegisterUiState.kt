package com.pegio.auth.presentation.screen.register

import android.net.Uri
import androidx.annotation.StringRes
import com.pegio.common.presentation.model.UiUser

data class RegisterUiState(
    val user: UiUser = UiUser.EMPTY,
    val selectedImageUri: Uri? = null,
    val validationError: RegisterValidationError = RegisterValidationError()
)

data class RegisterValidationError(
    @StringRes val username: Int? = null,
    @StringRes val age: Int? = null,
    @StringRes val gender: Int? = null,
    @StringRes val height: Int? = null,
    @StringRes val weight: Int? = null,
)