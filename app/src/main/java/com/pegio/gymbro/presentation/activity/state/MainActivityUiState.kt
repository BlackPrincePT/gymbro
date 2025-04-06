package com.pegio.gymbro.presentation.activity.state

import com.pegio.gymbro.presentation.activity.TopBarState
import com.pegio.gymbro.presentation.model.UiUser

data class MainActivityUiState(
    val topBarState: TopBarState = TopBarState(),
    val currentUser: UiUser? = null
)