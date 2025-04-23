package com.pegio.feed.presentation.screen.profile.state

import com.pegio.model.FollowRecord

sealed interface ProfileUiEffect {

    // Navigation
    data object NavigateBack : ProfileUiEffect
    data class NavigateToFollowRecord(val userId: String, val mode: FollowRecord.Type) : ProfileUiEffect
}