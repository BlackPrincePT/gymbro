package com.pegio.gymbro.activity.state

import com.pegio.common.presentation.model.UiUser
import com.pegio.gymbro.activity.TopBarState

data class MainActivityUiState(
    val topBarState: TopBarState = TopBarState(),
    val currentUser: UiUser? = null
)