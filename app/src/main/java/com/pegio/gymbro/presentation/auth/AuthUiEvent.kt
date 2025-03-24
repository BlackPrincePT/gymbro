package com.pegio.gymbro.presentation.auth

import android.content.Context

sealed interface AuthUiEvent {
    data class OnLaunchGoogleAuthOptions(val context: Context) : AuthUiEvent
    data object OnContinueAsGuest : AuthUiEvent
}