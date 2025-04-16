package com.pegio.settings.presentation.screen

import com.pegio.common.presentation.model.UiUser

data class AccountUiState(
    val user: UiUser = UiUser.DEFAULT
)