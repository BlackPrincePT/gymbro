package com.pegio.gymbro.presentation.screen.home

sealed interface HomeUiEvent {
    data object OnCreatePostClick: HomeUiEvent
    data object OnLoadMorePosts: HomeUiEvent

    // Top Bar
    data object OnChatClick: HomeUiEvent
    data object OnDrawerClick: HomeUiEvent
}