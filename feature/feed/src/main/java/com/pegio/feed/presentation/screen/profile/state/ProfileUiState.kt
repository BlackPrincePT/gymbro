package com.pegio.feed.presentation.screen.profile.state

import com.pegio.common.presentation.model.UiUser
import com.pegio.feed.presentation.model.UiPost
import com.pegio.feed.presentation.screen.profile.ProfileEditMode

data class ProfileUiState(

    // Loading
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val isBackgroundLoading: Boolean = false,
    val isAvatarLoading: Boolean = false,

    // Main
    val displayedUser: UiUser = UiUser.EMPTY,
    val userPosts: List<UiPost> = emptyList(),
    val editMode: ProfileEditMode? = null,

    // Compose State
    val isProfileOwner: Boolean = false,
    val isFollowing: Boolean = false,
    val endOfPostsReached: Boolean = false,
    val shouldShowBottomSheet: Boolean = false
)