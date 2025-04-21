package com.pegio.feed.presentation.screen.profile.state

import com.pegio.common.presentation.model.UiUser
import com.pegio.feed.presentation.model.UiPost

data class ProfileUiState(

    // Loading
    val isLoading: Boolean = false,

    // Main
    val displayedUser: UiUser = UiUser.EMPTY,
    val userPosts: List<UiPost> = emptyList(),

    // Compose State
    val isProfileOwner: Boolean = false,
    val isFollowing: Boolean = false,
    val endOfPostsReached: Boolean = false
)