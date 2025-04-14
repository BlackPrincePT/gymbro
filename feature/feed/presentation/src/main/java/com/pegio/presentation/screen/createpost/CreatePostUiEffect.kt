package com.pegio.presentation.screen.createpost

sealed interface CreatePostUiEffect {
    data object NavigateBack: CreatePostUiEffect
}