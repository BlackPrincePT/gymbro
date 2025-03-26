package com.pegio.gymbro.presentation.screen.account

import com.pegio.gymbro.domain.model.User

data class AccountUiState(
    val user: User = User(id = "" , username = "", profile = null)
)