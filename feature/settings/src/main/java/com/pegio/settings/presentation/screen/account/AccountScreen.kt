package com.pegio.settings.presentation.screen.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.components.EditAvatarContent
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.rememberGalleryLauncher
import com.pegio.designsystem.component.GymBroDropdownMenu
import com.pegio.designsystem.component.FormTextField
import com.pegio.designsystem.component.GymBroTextButton
import com.pegio.model.User.Gender
import com.pegio.settings.R
import com.pegio.settings.presentation.screen.account.state.AccountUiEffect
import com.pegio.settings.presentation.screen.account.state.AccountUiEvent
import com.pegio.settings.presentation.screen.account.state.AccountUiState

@Composable
fun AccountScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val launchGallery = rememberGalleryLauncher(
        onImageSelected = { viewModel.onEvent(AccountUiEvent.OnPhotoSelected(imageUri = it)) }
    )

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Main
            AccountUiEffect.LaunchGallery -> launchGallery()
            AccountUiEffect.ClearFocus -> focusManager.clearFocus()

            // Navigation
            AccountUiEffect.NavigateBack -> onBackClick.invoke()
        }
    }

    AccountContent(state = viewModel.uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun AccountContent(
    state: AccountUiState,
    onEvent: (AccountUiEvent) -> Unit
) = with(state) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        EditAvatarContent(
            imageUrl = user.avatarUrl,
            isLoading = isLoadingAvatar,
            onClick = { onEvent(AccountUiEvent.OnLaunchGallery) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        FormTextField(
            value = formValue.username,
            onValueChange = { onEvent(AccountUiEvent.OnUsernameChanged(it)) },
            label = user.username,
            error = validationError.username,
            trailingIcon = {
                SubmitButton(
                    condition = formValue.username.isNotEmpty() && user.username != formValue.username,
                    onClick = { onEvent(AccountUiEvent.OnUsernameSubmit) }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormTextField(
            value = formValue.age,
            onValueChange = { onEvent(AccountUiEvent.OnAgeChanged(it)) },
            label = user.age,
            error = validationError.age,
            trailingIcon = {
                SubmitButton(
                    condition = formValue.age.isNotEmpty() && user.age != formValue.age,
                    onClick = { onEvent(AccountUiEvent.OnAgeSubmit) }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        GymBroDropdownMenu(
            isExpanded = isGenderMenuExpanded,
            options = Gender.entries,
            label = user.gender.name,
            onSelectionChanged = { onEvent(AccountUiEvent.OnGenderChanged(it)) },
            onExpandedChange = { onEvent(AccountUiEvent.OnGenderMenuExpandedChange(it)) },
            selectedOption = formValue.gender?.name,
            trailingIcon = {
                SubmitButton(
                    condition = formValue.gender != null && user.gender != formValue.gender,
                    onClick = {
                        onEvent(AccountUiEvent.OnGenderSubmit)
                        onEvent(AccountUiEvent.OnGenderMenuExpandedChange(false))
                    }
                )
            },
            errorRes = validationError.gender,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormTextField(
            value = formValue.height,
            onValueChange = { onEvent(AccountUiEvent.OnHeightChanged(it)) },
            label = user.heightCm,
            error = validationError.height,
            trailingIcon = {
                SubmitButton(
                    condition = formValue.height.isNotEmpty() && user.heightCm != formValue.height,
                    onClick = { onEvent(AccountUiEvent.OnHeightSubmit) }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormTextField(
            value = formValue.weight,
            onValueChange = { onEvent(AccountUiEvent.OnWeightChanged(it)) },
            label = user.weightKg,
            error = validationError.weight,
            trailingIcon = {
                SubmitButton(
                    condition = formValue.weight.isNotEmpty() && user.heightCm != formValue.height,
                    onClick = { onEvent(AccountUiEvent.OnWeightSubmit) }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SubmitButton(
    condition: Boolean,
    onClick: () -> Unit
) {
    if (condition) GymBroTextButton(
        text = stringResource(R.string.feature_settings_submit),
        onClick = onClick
    )
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (AccountUiEvent) -> Unit
) {
    val title = stringResource(id = R.string.feature_settings_account)

    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                title = title,
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(AccountUiEvent.OnBackClick) }
                )
            )
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Preview(showBackground = true)
@Composable
private fun AccountContentPreview() {
    AccountContent(state = AccountUiState(), onEvent = { })
}