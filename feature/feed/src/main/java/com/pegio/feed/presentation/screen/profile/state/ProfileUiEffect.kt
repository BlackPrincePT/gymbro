package com.pegio.feed.presentation.screen.profile.state

import androidx.annotation.StringRes
import com.pegio.model.FollowRecord

sealed interface ProfileUiEffect {

    // Image
    data object LaunchGallery : ProfileUiEffect

    // Failure
    data class ShowSnackbar(@StringRes val errorRes: Int) : ProfileUiEffect

    // Navigation
    data object NavigateBack : ProfileUiEffect
    data class NavigateToFollowRecord(val userId: String, val mode: FollowRecord.Type) : ProfileUiEffect
    data class NavigateToCreatePost(val shouldOpenGallery: Boolean) : ProfileUiEffect
    data class NavigateToPostDetail(val postId: String) : ProfileUiEffect
    data class NavigateToWorkout(val workoutId: String) : ProfileUiEffect
}