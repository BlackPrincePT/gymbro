package com.pegio.auth.presentation.screen.auth

import android.content.Context
import com.pegio.auth.presentation.screen.auth.state.AuthUiEffect
import com.pegio.auth.presentation.screen.auth.state.AuthUiEvent
import com.pegio.auth.presentation.screen.auth.state.AuthUiState
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.core.onSuccessAsync
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.domain.usecase.auth.SignInAnonymouslyUseCase
import com.pegio.domain.usecase.common.CheckUserRegistrationStatusUseCase
import com.pegio.domain.usecase.common.LaunchGoogleAuthOptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val launchGoogleAuthOptions: LaunchGoogleAuthOptionsUseCase,
    private val signInAnonymously: SignInAnonymouslyUseCase,
    private val checkUserRegistrationStatus: CheckUserRegistrationStatusUseCase
) : BaseViewModel<AuthUiState, AuthUiEffect, AuthUiEvent>(initialState = AuthUiState()) {

    override fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.OnLaunchGoogleAuthOptions -> handleLaunchGoogleAuthOptions(context = event.context)
            AuthUiEvent.OnContinueAsGuest -> handleSignInAnonymously()
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun handleLaunchGoogleAuthOptions(context: Context) = launchWithLoading {
        launchGoogleAuthOptions(context)
            .onSuccessAsync { checkForSavedAuthState() }
            .onFailure { sendEffect(AuthUiEffect.Failure(error = it)) }
    }

    private fun handleSignInAnonymously() = launchWithLoading {
        signInAnonymously()
            .onSuccess { sendEffect(AuthUiEffect.NavigateToHome) }
            .onFailure { sendEffect(AuthUiEffect.Failure(error = it)) }
    }

    private suspend fun checkForSavedAuthState() {
        checkUserRegistrationStatus()
            .onSuccess { isComplete ->
                val navigationEffect = if (isComplete) AuthUiEffect.NavigateToHome else AuthUiEffect.NavigateToRegister
                sendEffect(navigationEffect)
            }
            .onFailure {  } // TODO: SHOW SNACKBAR
    }
}