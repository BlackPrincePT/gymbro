package com.pegio.auth.presentation.screen.auth.state

import android.content.Context

sealed interface AuthUiEvent {
    data class OnLaunchGoogleAuthOptions(val context: Context) : AuthUiEvent
    data object OnContinueAsGuest : AuthUiEvent
}