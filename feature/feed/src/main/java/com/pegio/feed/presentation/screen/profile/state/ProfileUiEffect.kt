package com.pegio.feed.presentation.screen.profile.state

import android.net.Uri
import com.pegio.model.FollowRecord

sealed interface ProfileUiEffect {

    // Image
    data object LaunchGallery : ProfileUiEffect

    // Navigation
    data object NavigateBack : ProfileUiEffect
    data class NavigateToFollowRecord(val userId: String, val mode: FollowRecord.Type) : ProfileUiEffect
    data class NavigateToCreatePost(val shouldOpenGallery: Boolean) : ProfileUiEffect
    data class NavigateToPostDetail(val postId: String) : ProfileUiEffect
}