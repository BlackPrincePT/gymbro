package com.pegio.auth.presentation.screen.register

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pegio.designsystem.component.DropdownMenu
import com.pegio.designsystem.component.FormTextField
import com.pegio.common.presentation.components.ProfileImage
import com.pegio.model.User.Gender
import kotlinx.coroutines.flow.collectLatest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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
@ExperimentalMaterial3Api
fun RegisterForm(
    state: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit
) {
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onEvent(RegisterUiEvent.OnProfilePhotoSelected(imageUri = uri)) }
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                ProfileImage(
                    imageUrl = state.selectedImageUri?.toString(),
                    modifier = Modifier
                        .fillMaxSize()
                )

                if (state.selectedImageUri == null)
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        tint = Color.White,
                        contentDescription = "Camera",
                        modifier = Modifier
                            .size(50.dp)
                    )
            }

            Spacer(modifier = Modifier.width(16.dp))

            TextButton(onClick = { galleryLauncher.launch("image/*") }) {
                Text(text = "Click to choose profile picture")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        FormTextField(
            value = state.user.username,
            onValueChange = { onEvent(RegisterUiEvent.OnUsernameChanged(it)) },
            label = "Username",
            error = state.validationError.username,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormTextField(
            value = state.user.age,
            onValueChange = { onEvent(RegisterUiEvent.OnAgeChanged(it)) },
            label = "Age",
            isNumberOnly = true,
            error = state.validationError.age,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenu(
            options = Gender.entries,
            onSelectionChanged = { onEvent(RegisterUiEvent.OnGenderChanged(it)) },
            label = "Gender",
            selectedOption = state.user.gender.name,
            error = state.validationError.gender
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormTextField(
            value = state.user.heightCm,
            onValueChange = { onEvent(RegisterUiEvent.OnHeightChanged(it)) },
            label = "Height (cm)",
            isNumberOnly = true,
            error = state.validationError.height,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormTextField(
            value = state.user.weightKg,
            onValueChange = { onEvent(RegisterUiEvent.OnWeightChanged(it)) },
            label = "Weight (kg)",
            isNumberOnly = true,
            error = state.validationError.weight,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEvent(RegisterUiEvent.OnSubmit) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RegisterFormPreview() {
    RegisterForm(
        state = RegisterUiState(),
        onEvent = {}
    )
}