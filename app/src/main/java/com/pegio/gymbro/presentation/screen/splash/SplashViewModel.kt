package com.pegio.gymbro.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.usecase.auth.CheckUserRegistrationStatusUseCase
import com.pegio.gymbro.domain.usecase.splash.HasSavedAuthSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val hasSavedAuthSession: HasSavedAuthSessionUseCase,
    private val checkUserRegistrationStatus: CheckUserRegistrationStatusUseCase
) : ViewModel() {

    private val _uiEffect = Channel<SplashUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        checkForSavedAuthState()
    }

    private fun checkForSavedAuthState() = viewModelScope.launch {
        if (hasSavedAuthSession()) {
            checkUserRegistrationStatus(
                onRegistrationComplete = { sendEffect(SplashUiEffect.NavigateToHome) },
                onRegistrationIncomplete = { sendEffect(SplashUiEffect.NavigateToRegister) },
                onException = { sendEffect(SplashUiEffect.NavigateToAuth) }
            )
        } else {
            sendEffect(SplashUiEffect.NavigateToAuth)
        }
    }

    private fun sendEffect(effect: SplashUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.send(effect)
        }
    }
}