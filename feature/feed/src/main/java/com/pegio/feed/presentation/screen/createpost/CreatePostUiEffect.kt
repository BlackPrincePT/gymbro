package com.pegio.feed.presentation.screen.createpost

sealed interface CreatePostUiEffect {
    data object NavigateBack: CreatePostUiEffect
}