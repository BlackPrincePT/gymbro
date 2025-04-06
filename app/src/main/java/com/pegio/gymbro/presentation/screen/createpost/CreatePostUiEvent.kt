package com.pegio.gymbro.presentation.screen.createpost

sealed interface CreatePostUiEvent {
    data class OnPostTextChange(val value: String): CreatePostUiEvent
    data object OnPostClick: CreatePostUiEvent
    data object OnCancelClick: CreatePostUiEvent
}