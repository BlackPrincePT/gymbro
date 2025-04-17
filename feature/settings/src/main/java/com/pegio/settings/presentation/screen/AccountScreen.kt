package com.pegio.settings.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.designsystem.component.ProfileImage
import com.pegio.common.presentation.util.CollectLatestEffect

@Composable
fun AccountScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            AccountUiEffect.NavigateBack -> onBackClick.invoke()
        }
    }

    AccountContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier
    )
}

@Composable
private fun AccountContent(
    state: AccountUiState,
    onEvent: (AccountUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onEvent(AccountUiEvent.OnPhotoSelected(imageUri = uri)) }
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 32.dp)
        ) {
            ProfileImage(
                imageUrl = state.user.imgProfileUrl,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            TextButton(onClick = { galleryLauncher.launch("image/*") }) {
                Text(text = "Edit your profile picture")
            }
        }
    }
}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (AccountUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(AccountUiEvent.OnBackClick) }
                )
            )
        )
    }
}

@Preview
@Composable
private fun AccountContentPreview() {
    AccountContent(state = AccountUiState(), onEvent = { })
}