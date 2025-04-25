package com.pegio.feed.presentation.screen.followrecord.state

sealed interface FollowRecordUiEvent {

    // Main
    data object OnLoadMoreUsers : FollowRecordUiEvent

    // Navigation
    data object OnBackClick : FollowRecordUiEvent
    data class OnUserProfileClick(val userId: String) : FollowRecordUiEvent
}