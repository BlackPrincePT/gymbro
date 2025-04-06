package com.pegio.gymbro.presentation.screen.home

sealed interface HomeUiEvent {
    data object OnChatClick: HomeUiEvent
    data object OnAccountClick: HomeUiEvent
    data object OnSignOut : HomeUiEvent
    data object OnCreatePostClick: HomeUiEvent
    data object OnLoadMorePosts: HomeUiEvent
}