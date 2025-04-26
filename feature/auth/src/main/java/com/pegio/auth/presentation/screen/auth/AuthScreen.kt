package com.pegio.auth.presentation.screen.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.auth.presentation.screen.auth.state.AuthUiEffect
import com.pegio.auth.presentation.screen.auth.state.AuthUiEvent
import com.pegio.auth.presentation.screen.auth.state.AuthUiState
import com.pegio.common.presentation.util.CollectLatestEffect

@Composable
internal fun AuthScreen(
    onAuthSuccess: () -> Unit,
    onRegistrationRequired: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            AuthUiEffect.NavigateToHome -> onAuthSuccess()
            AuthUiEffect.NavigateToRegister -> onRegistrationRequired()
            is AuthUiEffect.ShowSnackbar -> onShowSnackbar(context.getString(effect.errorRes))
        }
    }

    AuthOptions(state = viewModel.uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun AuthOptions(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        if (state.isLoading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

        Column(modifier = Modifier.align(alignment = Alignment.BottomCenter)) {

            val buttonModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 8.dp)

            Button(
                onClick = { onEvent(AuthUiEvent.OnContinueAsGuest) },
                enabled = !state.isLoading,
                modifier = buttonModifier
            ) {
                Text("Continue as Guest")
            }

            Button(
                onClick = { onEvent(AuthUiEvent.OnLaunchGoogleAuthOptions(context)) },
                enabled = !state.isLoading,
                modifier = buttonModifier
            ) {
                Text("Sign in with Google")
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Preview
@Composable
private fun AuthOptionsPreview() {
    AuthOptions(
        state = AuthUiState(),
        onEvent = { }
    )
}