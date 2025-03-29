package com.pegio.gymbro.presentation.screen.register

import android.net.Uri
import com.pegio.gymbro.domain.core.ValidationError
import com.pegio.gymbro.presentation.model.UiUser

data class RegisterUiState(
    val user: UiUser = UiUser.EMPTY,
    val selectedImageUri: Uri? = null,
    val validationError: RegisterValidationError = RegisterValidationError()
)

data class RegisterValidationError(
    val username: ValidationError.Username? = null,
    val age: ValidationError.Age? = null,
    val gender: ValidationError.Gender? = null,
    val height: ValidationError.Height? = null,
    val weight: ValidationError.Weight? = null,
)