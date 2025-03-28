package com.pegio.gymbro.presentation.screen.account

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pegio.gymbro.presentation.components.ProfileImage
import com.pegio.gymbro.presentation.components.TopAppBarContent
import com.pegio.gymbro.presentation.theme.GymBroTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AccountScreen(
    onBackClick: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->

        }
    }

    Scaffold(
        topBar = { TopAppBarContent(onBackClick) },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        AccountContent(
            state = uiState,
            onEvent = viewModel::onEvent,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
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

            TextButton(onClick = { galleryLauncher.launch(input = "image/*") }) {
                Text(text = "Edit your profile picture")
            }
        }
    }
}

@Preview
@Composable
private fun AccountContentPreview() {
    GymBroTheme {
        AccountContent(state = AccountUiState(), onEvent = { })
    }
}