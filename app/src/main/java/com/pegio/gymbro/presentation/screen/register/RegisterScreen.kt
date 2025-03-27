package com.pegio.gymbro.presentation.screen.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pegio.gymbro.presentation.theme.GymBroTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                RegisterUiEffect.NavigateToHome -> onRegisterSuccess()
            }
        }
    }

    RegisterForm(state = uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun RegisterForm(
    state: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()

    ) {
        TextField(
            value = state.username,
            onValueChange = { onEvent(RegisterUiEvent.OnUsernameChanged(newValue = it)) },
            label = { Text(text = "Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEvent(RegisterUiEvent.OnRegister) }
        ) {
            Text(text = "Register")
        }
    }
}

@Preview
@Composable
private fun RegisterFormPreview() {
    GymBroTheme {
        RegisterForm(
            state = RegisterUiState(),
            onEvent = {}
        )
    }
}