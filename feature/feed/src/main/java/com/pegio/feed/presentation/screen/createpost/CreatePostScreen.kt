package com.pegio.feed.presentation.screen.createpost

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.components.ProfileImage
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiEffect
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiEvent
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiState

@Composable
internal fun CreatePostScreen(
    onDismiss: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is CreatePostUiEffect.ShowSnackbar ->
                onShowSnackbar(context.getString(effect.errorRes), null)

            // Top Bar
            CreatePostUiEffect.NavigateBack -> onDismiss.invoke()
        }
    }

    CreatePostContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier
    )
}

@Composable
private fun CreatePostContent(
    state: CreatePostUiState,
    onEvent: (CreatePostUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onEvent(CreatePostUiEvent.OnPhotoSelected(imageUri = uri)) }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        HorizontalDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "Choose your workout")

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }

        TextField(
            value = state.postText,
            onValueChange = { onEvent(CreatePostUiEvent.OnPostTextChange(value = it)) },
            label = { Text(text = "What's on your muscle?") },
            modifier = Modifier
                .fillMaxWidth()
        )

        state.imageUri?.let {
            ProfileImage(
                imageUrl = it.toString()
            )
        }
    }
}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (CreatePostUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.Default.Close,
                    onClick = { onEvent(CreatePostUiEvent.OnCancelClick) }
                ),
                actions = listOf(
                    TopBarAction(
                        icon = Icons.AutoMirrored.Default.Send,
                        onClick = { onEvent(CreatePostUiEvent.OnPostClick) }
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreatePostContentPreview() {
    CreatePostContent(state = CreatePostUiState(), onEvent = { })
}