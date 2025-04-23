package com.pegio.feed.presentation.screen.followrecord.state

sealed interface FollowRecordUiEvent {

    // Main
    data object OnLoadMoreUsers : FollowRecordUiEvent
    data class OnUserProfileClick(val userId: String) : FollowRecordUiEvent

    // Navigation
    data object OnBackClick : FollowRecordUiEvent
}