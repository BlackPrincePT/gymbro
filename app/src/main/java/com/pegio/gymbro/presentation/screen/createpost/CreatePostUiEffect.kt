package com.pegio.gymbro.presentation.screen.createpost

sealed interface CreatePostUiEffect {
    data object NavigateBack: CreatePostUiEffect
}