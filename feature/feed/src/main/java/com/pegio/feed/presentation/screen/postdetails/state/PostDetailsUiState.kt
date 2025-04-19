package com.pegio.feed.presentation.screen.postdetails.state

import com.pegio.feed.presentation.model.UiPost
import com.pegio.feed.presentation.model.UiPostComment

data class PostDetailsUiState(

    // Loading
    val isLoading: Boolean = false,

    // Post
    val displayedPost: UiPost = UiPost.DEFAULT,
    val comments: List<UiPostComment> = emptyList(),

    // Compose State
    val commentText: String = ""
)