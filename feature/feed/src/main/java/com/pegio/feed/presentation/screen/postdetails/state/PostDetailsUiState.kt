package com.pegio.feed.presentation.screen.postdetails.state

import com.pegio.feed.presentation.model.UiPost
import com.pegio.feed.presentation.model.UiPostComment

data class PostDetailsUiState(

    // Loading
    val isLoading: Boolean = false,

    // Main
    val displayedPost: UiPost = UiPost.DEFAULT,
    val comments: List<UiPostComment> = emptyList(),
    val endOfCommentsReached: Boolean = false,

    // Compose State
    val commentText: String = ""
)