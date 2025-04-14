package com.pegio.presentation.screen.createpost

sealed interface HomeUiEffect {
    data object NavigateToCreatePost : HomeUiEffect

    // Top Bar
    data object OpenDrawer : HomeUiEffect
    data object NavigateToChat : HomeUiEffect
}