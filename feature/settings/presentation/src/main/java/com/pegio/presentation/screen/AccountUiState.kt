package com.pegio.presentation.screen

import com.pegio.presentation.model.UiUser

data class AccountUiState(
    val user: UiUser = UiUser.DEFAULT
)