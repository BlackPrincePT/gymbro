package com.pegio.gymbro.presentation.screen.home

sealed interface HomeUiEffect {
    data object NavigateToCreatePost : HomeUiEffect

    // Top Bar
    data object OpenDrawer : HomeUiEffect
    data object NavigateToChat : HomeUiEffect
}