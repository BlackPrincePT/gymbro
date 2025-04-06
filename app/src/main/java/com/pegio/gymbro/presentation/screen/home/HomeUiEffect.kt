package com.pegio.gymbro.presentation.screen.home

sealed interface HomeUiEffect {
    data object NavigateToAccount: HomeUiEffect
    data object NavigateToChat: HomeUiEffect
    data object NavigateToCreatePost: HomeUiEffect
    data object SignedOutSuccessfully : HomeUiEffect
}