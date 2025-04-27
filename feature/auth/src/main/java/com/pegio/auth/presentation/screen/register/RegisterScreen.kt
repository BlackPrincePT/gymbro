package com.pegio.auth.presentation.screen.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.auth.R
import com.pegio.auth.presentation.screen.register.state.RegisterUiEffect
import com.pegio.auth.presentation.screen.register.state.RegisterUiEvent
import com.pegio.auth.presentation.screen.register.state.RegisterUiState
import com.pegio.common.presentation.components.EditAvatarContent
import com.pegio.common.presentation.components.EmptyLoadingScreen
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.rememberGalleryLauncher
import com.pegio.designsystem.component.DropdownMenu
import com.pegio.designsystem.component.FormTextField
import com.pegio.designsystem.component.GymBroButton
import com.pegio.model.User.Gender
import kotlinx.coroutines.launch
import com.pegio.common.R as cR

@Composable
internal fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val launchGallery = rememberGalleryLauncher(
        onImageSelected = { viewModel.onEvent(RegisterUiEvent.OnProfilePhotoSelected(it)) }
    )

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Main
            RegisterUiEffect.LaunchGallery -> launchGallery()

            // Navigation
            RegisterUiEffect.NavigateToHome -> onRegisterSuccess()
        }
    }

    with(viewModel) {
        when {
            uiState.isLoading -> EmptyLoadingScreen()
            else -> {
                RegisterContent(state = uiState, onEvent = viewModel::onEvent)

                if (uiState.shouldShowBottomSheet)
                    BottomSheetContent(
                        onChooseFromGalleryClick = { onEvent(RegisterUiEvent.OnLaunchGallery) },
                        onRemoveClick = { onEvent(RegisterUiEvent.OnProfilePhotoSelected(imageUri = null)) },
                        onBottomSheetClose = {
                            onEvent(RegisterUiEvent.OnBottomSheetStateUpdate(shouldShow = false))
                        }
                    )
            }
        }
    }
}

@Composable
private fun RegisterContent(
    state: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit
) = with(state) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        EditAvatarContent(
            imageUrl = selectedImageUri?.toString(),
            isLoading = false,
            onClick = { onEvent(RegisterUiEvent.OnLaunchGallery) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        FormTextField(
            value = formValue.username,
            onValueChange = { onEvent(RegisterUiEvent.OnUsernameChanged(it)) },
            label = stringResource(id = R.string.feature_auth_username),
            error = validationError.username,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormTextField(
            value = formValue.age,
            onValueChange = { onEvent(RegisterUiEvent.OnAgeChanged(it)) },
            label = stringResource(id = R.string.feature_auth_age),
            isNumberOnly = true,
            error = validationError.age,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenu(
            options = Gender.entries,
            onSelectionChanged = { onEvent(RegisterUiEvent.OnGenderChanged(it)) },
            label = stringResource(id = R.string.feature_auth_gender),
            selectedOption = formValue.gender?.name,
            error = validationError.gender
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormTextField(
            value = formValue.height,
            onValueChange = { onEvent(RegisterUiEvent.OnHeightChanged(it)) },
            label = stringResource(id = R.string.feature_auth_height_cm),
            isNumberOnly = true,
            error = validationError.height,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormTextField(
            value = formValue.weight,
            onValueChange = { onEvent(RegisterUiEvent.OnWeightChanged(it)) },
            label = stringResource(id = R.string.feature_auth_weight_kg),
            isNumberOnly = true,
            error = validationError.weight,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(64.dp))

        GymBroButton(
            onClick = { onEvent(RegisterUiEvent.OnSubmit) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.feature_auth_register))
        }
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun BottomSheetContent(
    onChooseFromGalleryClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onBottomSheetClose: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val fnHideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onBottomSheetClose()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onBottomSheetClose() },
        sheetState = sheetState,
        dragHandle = null
    ) {
        GymBroButton(
            onClick = {
                onChooseFromGalleryClick()
                fnHideSheet.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
        ) {
            Text(text = stringResource(cR.string.feature_common_choose_from_gallery))
        }

        GymBroButton(
            onClick = {
                onRemoveClick()
                fnHideSheet.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(text = stringResource(cR.string.feature_common_remove))
        }
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Preview(showBackground = true)
@Composable
private fun RegisterFormPreview() {
    RegisterContent(state = RegisterUiState(), onEvent = { })
}