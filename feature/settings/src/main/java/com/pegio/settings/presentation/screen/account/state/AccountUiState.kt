package com.pegio.settings.presentation.screen.account.state

import androidx.annotation.StringRes
import com.pegio.common.presentation.model.UiUser
import com.pegio.model.User.Gender

data class AccountUiState(

    // Main
    val user: UiUser = UiUser.DEFAULT,

    // Loading
    val isLoadingAvatar: Boolean = false,

    // Compose State
    val formValue: AccountFormValue = AccountFormValue(),
    val validationError: AccountValidationError = AccountValidationError()
)

data class AccountFormValue(
    val username: String = "",
    val age: String = "",
    val gender: Gender? = null,
    val height: String = "",
    val weight: String = "",
)

data class AccountValidationError(
    @StringRes val username: Int? = null,
    @StringRes val age: Int? = null,
    @StringRes val gender: Int? = null,
    @StringRes val height: Int? = null,
    @StringRes val weight: Int? = null,
)