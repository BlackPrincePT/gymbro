package com.pegio.auth.presentation.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.auth.R
import com.pegio.auth.presentation.screen.auth.state.AuthUiEffect
import com.pegio.auth.presentation.screen.auth.state.AuthUiEvent
import com.pegio.auth.presentation.screen.auth.state.AuthUiState
import com.pegio.common.presentation.components.EmptyLoadingScreen
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.component.GymBroButton
import com.pegio.common.R as cR

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

    with(viewModel.uiState) {
        when {
            isLoading -> EmptyLoadingScreen()
            else -> AuthOptions(state = this, onEvent = viewModel::onEvent)
        }
    }
}

@Composable
private fun AuthOptions(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit
) {
    val context = LocalContext.current

    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp)
    ) {

        Text(
            text = stringResource(id = R.string.feature_auth_warning),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.feature_auth_welcome_message),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        AuthButton(
            onClick = { onEvent(AuthUiEvent.OnContinueAsGuest) },
            enabled = !state.isLoading,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            text = stringResource(R.string.feature_auth_continue_as_guest),
            modifier = buttonModifier
        )

        AuthButton(
            onClick = { onEvent(AuthUiEvent.OnLaunchGoogleAuthOptions(context)) },
            enabled = !state.isLoading,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = cR.drawable.feature_common_ic_google_logo),
                    contentDescription = "Google logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
            },
            text = stringResource(id = cR.string.feature_common_sign_in_with_google),
            modifier = buttonModifier
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun AuthButton(
    onClick: () -> Unit,
    enabled: Boolean,
    leadingIcon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    GymBroButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon()

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
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