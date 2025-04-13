package com.pegio.gymbro.presentation.screen.auth

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.pegio.gymbro.BuildConfig
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.onFailure
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.usecase.common.SignInAnonymouslyUseCase
import com.pegio.gymbro.domain.usecase.common.SignInWithGoogleUseCase
import com.pegio.gymbro.domain.usecase.common.CheckUserRegistrationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithGoogle: SignInWithGoogleUseCase,
    private val signInAnonymously: SignInAnonymouslyUseCase,
    private val checkUserRegistrationStatus: CheckUserRegistrationStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AuthUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.OnLaunchGoogleAuthOptions -> launchGoogleAuthOptions(context = event.context)
            AuthUiEvent.OnContinueAsGuest -> continueAsGuest()
        }
    }

    private fun launchGoogleAuthOptions(context: Context) = viewModelScope.launch {
        try {
            val signInWithGoogleOption = GetSignInWithGoogleOption
                .Builder(BuildConfig.DEFAULT_WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val result = CredentialManager.create(context)
                .getCredential(request = request, context = context)

            createTokenWithCredentials(result.credential)

        } catch (e: CancellationException) {
            throw e // Propagate coroutine cancellations immediately.
        }
    }

    private suspend fun createTokenWithCredentials(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            signInWithGoogle(googleIdTokenCredential.idToken)
                .onSuccess { checkForSavedAuthState() }
                .onFailure { sendEffect(AuthUiEffect.Failure(error = it)) }
        } else {
            sendEffect(AuthUiEffect.Failure(error = DataError.Auth.INVALID_CREDENTIAL))
        }
    }

    private fun continueAsGuest() = viewModelScope.launch {
        signInAnonymously()
            .onSuccess { sendEffect(AuthUiEffect.NavigateToHome) }
            .onFailure { sendEffect(AuthUiEffect.Failure(error = it)) }
    }

    private fun checkForSavedAuthState() = viewModelScope.launch {
        checkUserRegistrationStatus()
            .onSuccess { isComplete ->
                val navigationEffect = if (isComplete) AuthUiEffect.NavigateToHome else AuthUiEffect.NavigateToRegister
                sendEffect(navigationEffect)
            }
    }

    private fun sendEffect(effect: AuthUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}