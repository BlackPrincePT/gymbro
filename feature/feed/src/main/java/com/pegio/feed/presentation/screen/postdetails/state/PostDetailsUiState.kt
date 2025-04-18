package com.pegio.feed.presentation.screen.postdetails.state

import com.pegio.common.presentation.model.UiUser
import com.pegio.feed.presentation.model.UiComment
import com.pegio.feed.presentation.model.UiPost

data class PostDetailsUiState(

    // Loading
    val isLoading: Boolean = false,

    // Post
    val postAuthor: UiUser? = null,
    val displayedPost: UiPost? = null,
    val comments: List<UiComment> = emptyList()
)