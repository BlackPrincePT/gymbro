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
import com.pegio.gymbro.domain.usecase.auth.SignInWithGoogleUseCase
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.usecase.auth.CheckUserRegistrationStatusUseCase
import com.pegio.gymbro.domain.usecase.auth.SignInAnonymouslyUseCase
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

            when (val signInResult = signInWithGoogle(googleIdTokenCredential.idToken)) {
                is Resource.Success -> checkForSavedAuthState()
                is Resource.Failure -> sendEffect(AuthUiEffect.Failure(e = signInResult.error))
            }
        } else {
            sendEffect(AuthUiEffect.Failure(e = DataError.Firebase.INVALID_CREDENTIAL))
        }
    }

    private fun continueAsGuest() = viewModelScope.launch {
        when (val signInResult = signInAnonymously()) {
            is Resource.Success -> sendEffect(AuthUiEffect.NavigateToHome)
            is Resource.Failure -> sendEffect(AuthUiEffect.Failure(e = signInResult.error))
        }
    }

    private fun checkForSavedAuthState() = viewModelScope.launch {
        checkUserRegistrationStatus(
            onRegistrationComplete = { sendEffect(AuthUiEffect.NavigateToHome) },
            onRegistrationIncomplete = { sendEffect(AuthUiEffect.NavigateToRegister) }
        )
    }

    private fun sendEffect(effect: AuthUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}