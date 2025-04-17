package com.pegio.auth.presentation.screen.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.domain.usecase.auth.SignInAnonymouslyUseCase
import com.pegio.domain.usecase.common.CheckUserRegistrationStatusUseCase
import com.pegio.domain.usecase.common.LaunchGoogleAuthOptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val launchGoogleAuthOptions: LaunchGoogleAuthOptionsUseCase,
    private val signInAnonymously: SignInAnonymouslyUseCase,
    private val checkUserRegistrationStatus: CheckUserRegistrationStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AuthUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.OnLaunchGoogleAuthOptions -> handleLaunchGoogleAuthOptions(context = event.context)
            AuthUiEvent.OnContinueAsGuest -> continueAsGuest()
        }
    }

    private fun handleLaunchGoogleAuthOptions(context: Context) = viewModelScope.launch {
        launchGoogleAuthOptions(context)
            .onSuccess { checkForSavedAuthState() }
            .onFailure { sendEffect(AuthUiEffect.Failure(error = it)) }
    }

    private fun continueAsGuest() = viewModelScope.launch {
        signInAnonymously()
            .onSuccess { sendEffect(AuthUiEffect.NavigateToHome) }
            .onFailure { sendEffect(AuthUiEffect.Failure(error = it)) }
    }

    private fun checkForSavedAuthState() = viewModelScope.launch {
        checkUserRegistrationStatus()
            .onSuccess { isComplete ->
                val navigationEffect =
                    if (isComplete) AuthUiEffect.NavigateToHome else AuthUiEffect.NavigateToRegister
                sendEffect(navigationEffect)
            }
    }

    private fun sendEffect(effect: AuthUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}