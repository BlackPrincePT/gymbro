package com.pegio.presentation.screen.createpost

sealed interface HomeUiEvent {
    data object OnCreatePostClick: HomeUiEvent
    data object OnLoadMorePosts: HomeUiEvent

    // Top Bar
    data object OnChatClick: HomeUiEvent
    data object OnDrawerClick: HomeUiEvent
}