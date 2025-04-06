package com.pegio.gymbro.presentation.screen.home

import com.pegio.gymbro.presentation.model.UiPost
import com.pegio.gymbro.presentation.model.UiUser

data class HomeUiState(
    val currentUser: UiUser = UiUser.DEFAULT,
    val relevantPosts: List<UiPost> = emptyList()
)