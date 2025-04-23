package com.pegio.feed.presentation.screen.followrecord.state

sealed interface FollowRecordUiEffect {

    // Navigation
    data object NavigateBack : FollowRecordUiEffect
    data class NavigateToUserProfile(val userId: String) : FollowRecordUiEffect
}