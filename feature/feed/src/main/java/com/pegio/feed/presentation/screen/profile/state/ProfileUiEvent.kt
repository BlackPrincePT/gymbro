package com.pegio.feed.presentation.screen.profile.state

import android.net.Uri
import com.pegio.feed.presentation.screen.profile.ProfileEditMode
import com.pegio.model.FollowRecord
import com.pegio.model.Vote

sealed interface ProfileUiEvent {

    // Main
    data class OnEditModeChange(val mode: ProfileEditMode) : ProfileUiEvent
    data class OnPostVote(val postId: String, val voteType: Vote.Type) : ProfileUiEvent
    data object OnLoadMorePosts : ProfileUiEvent
    data object OnFollowClick : ProfileUiEvent

    // Image
    data object OnOpenGallery : ProfileUiEvent
    data class OnAvatarSelected(val uri: Uri) : ProfileUiEvent
    data class OnBackgroundSelected(val uri: Uri) : ProfileUiEvent
    data object OnAvatarRemove : ProfileUiEvent
    data object OnBackgroundRemove : ProfileUiEvent

    // Navigation
    data object OnBackClick : ProfileUiEvent
    data class OnFollowRecordClick(val userId: String, val mode: FollowRecord.Type) : ProfileUiEvent
    data class OnCreatePostClick(val shouldOpenGallery: Boolean) : ProfileUiEvent
    data class OnPostCommentClick(val postId: String) : ProfileUiEvent

    // Bottom Sheet
    data class OnBottomSheetStateUpdate(val shouldShow: Boolean) : ProfileUiEvent
}