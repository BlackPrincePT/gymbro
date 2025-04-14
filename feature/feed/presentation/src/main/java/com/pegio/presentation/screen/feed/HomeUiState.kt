package com.pegio.presentation.screen.feed

import com.pegio.presentation.model.UiPost
import com.pegio.presentation.model.UiUser

data class HomeUiState(
    val currentUser: UiUser = UiUser.DEFAULT,
    val relevantPosts: List<UiPost> = emptyList()
)