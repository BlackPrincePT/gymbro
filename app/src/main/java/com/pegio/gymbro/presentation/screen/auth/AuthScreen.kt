package com.pegio.gymbro.presentation.screen.auth

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
import androidx.navigation.NavController
import com.pegio.gymbro.presentation.core.Route
import com.pegio.gymbro.presentation.theme.GymBroTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                AuthUiEffect.NavigateToHome -> navController.navigate(Route.HomeScreen)
                AuthUiEffect.NavigateToRegister -> navController.navigate(Route.RegisterScreen)
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
    GymBroTheme {
        AuthOptions(
            state = AuthUiState(),
            onEvent = {}
        )
    }
}