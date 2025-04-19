package com.pegio.feed.presentation.screen.postdetails.state

sealed interface PostDetailsUiEffect {

    // Navigation
    data object NavigateBack : PostDetailsUiEffect

    // Snackbar
    data object Show
}