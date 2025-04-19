package com.pegio.feed.presentation.screen.createpost.state

import androidx.annotation.StringRes

sealed interface CreatePostUiEffect {

    // Failure
    data class ShowSnackbar(@StringRes val errorRes: Int): CreatePostUiEffect

    // Top Bar
    data object NavigateBack: CreatePostUiEffect
}