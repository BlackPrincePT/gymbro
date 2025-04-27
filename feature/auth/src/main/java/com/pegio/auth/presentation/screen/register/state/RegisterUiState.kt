package com.pegio.auth.presentation.screen.register.state

import android.net.Uri
import androidx.annotation.StringRes
import com.pegio.model.User.Gender

data class RegisterUiState(

    // Loading
    val isLoading: Boolean = false,

    // Compose State
    val selectedImageUri: Uri? = null,
    val formValue: RegisterFormValue = RegisterFormValue(),
    val validationError: RegisterValidationError = RegisterValidationError()
)

data class RegisterFormValue(
    val username: String = "",
    val age: String = "",
    val gender: Gender? = null,
    val height: String = "",
    val weight: String = "",
)

data class RegisterValidationError(
    @StringRes val username: Int? = null,
    @StringRes val age: Int? = null,
    @StringRes val gender: Int? = null,
    @StringRes val height: Int? = null,
    @StringRes val weight: Int? = null,
)