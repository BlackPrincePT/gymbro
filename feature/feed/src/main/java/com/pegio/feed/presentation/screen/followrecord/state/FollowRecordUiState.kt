package com.pegio.feed.presentation.screen.followrecord.state

import com.pegio.common.presentation.model.UiUser

data class FollowRecordUiState(

    // Loading
    val isLoading: Boolean = false,

    // Main
    val users: List<UiUser> = emptyList(),

    // Compose State
    val endOfUsersReached: Boolean = false
)