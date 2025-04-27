package com.pegio.gymbro.activity.state

import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.state.TopBarState
import com.pegio.designsystem.theme.ThemeMode

data class MainActivityUiState(

    // App
    val topBarState: TopBarState = TopBarState(),
    val themeMode: ThemeMode = ThemeMode.SYSTEM,

    // User
    val currentUser: UiUser? = null,
    val isAnonymous: Boolean = false
)