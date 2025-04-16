package com.pegio.auth.presentation.screen.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    onAuthSuccessAndRegistrationComplete: () -> Unit,
    onAuthSuccessButRegistrationIncomplete: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                AuthUiEffect.NavigateToHome -> onAuthSuccessAndRegistrationComplete()
                AuthUiEffect.NavigateToRegister -> onAuthSuccessButRegistrationIncomplete()
                is AuthUiEffect.Failure -> {}
            }
        }
    }

    AuthOptions(state = uiState, onEvent = viewModel::onEvent)
}

@Composable
fun AuthOptions(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(alignment = Alignment.BottomCenter)) {

            val buttonModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 8.dp)

            Button(
                onClick = { onEvent(AuthUiEvent.OnContinueAsGuest) },
                modifier = buttonModifier
            ) {
                Text("Continue as Guest")
            }

            Button(
                onClick = { onEvent(AuthUiEvent.OnLaunchGoogleAuthOptions(context)) },
                modifier = buttonModifier
            ) {
                Text("Sign in with Google")
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview
@Composable
private fun AuthOptionsPreview() {
    AuthOptions(
        state = AuthUiState(),
        onEvent = {}
    )
}