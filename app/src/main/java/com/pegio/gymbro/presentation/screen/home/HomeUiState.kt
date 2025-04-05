package com.pegio.gymbro.presentation.screen.home

import com.pegio.gymbro.presentation.model.UiPost
import com.pegio.gymbro.presentation.model.UiUser

data class HomeUiState(
    val currentUser: UiUser = UiUser.DEFAULT,
    val relevantPosts: List<UiPost> = List(size = 6) { UiPost.DEFAULT }
)