package com.pegio.feed.presentation.screen.feed

import com.pegio.feed.presentation.model.UiPost
import com.pegio.common.presentation.model.UiUser

data class HomeUiState(
    val currentUser: UiUser = UiUser.DEFAULT,
    val relevantPosts: List<UiPost> = emptyList()
)