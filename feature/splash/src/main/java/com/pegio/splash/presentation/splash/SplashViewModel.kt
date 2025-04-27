package com.pegio.splash.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.SessionError
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.domain.usecase.auth.CheckUserAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserAuthState: CheckUserAuthStateUseCase
) : ViewModel() {

    private val _uiEffect = Channel<SplashUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        checkForSavedAuthState()
    }

    private fun checkForSavedAuthState() = viewModelScope.launch {
        checkUserAuthState()
            .onSuccess { sendEffect(SplashUiEffect.NavigateToHome) }
            .onFailure { error ->
                when (error) {
                    SessionError.AnonymousUser -> sendEffect(SplashUiEffect.NavigateToHome)
                    SessionError.RegistrationIncomplete -> sendEffect(SplashUiEffect.NavigateToRegister)
                    SessionError.Unauthenticated -> sendEffect(SplashUiEffect.NavigateToAuth)
                    SessionError.Unknown -> throw Exception("SplashViewModel: $error")
                }
            }
    }

    private fun sendEffect(effect: SplashUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.send(effect)
        }
    }
}