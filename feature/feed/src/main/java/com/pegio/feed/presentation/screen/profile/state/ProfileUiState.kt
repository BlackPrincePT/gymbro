package com.pegio.feed.presentation.screen.profile.state

import com.pegio.common.presentation.model.UiUser

data class ProfileUiState(

    // Loading
    val isLoading: Boolean = false,

    // Main
    val displayedUser: UiUser = UiUser.EMPTY
)