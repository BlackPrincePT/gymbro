package com.pegio.gymbro.presentation.screen.home

import com.pegio.gymbro.presentation.model.UiUser

data class HomeUiState(
    val displayedUser: UiUser = UiUser.DEFAULT
)