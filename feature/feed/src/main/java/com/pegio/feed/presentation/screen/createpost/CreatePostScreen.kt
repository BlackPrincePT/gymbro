package com.pegio.feed.presentation.screen.createpost

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.rememberGalleryLauncher
import com.pegio.designsystem.component.GymBroIconButton
import com.pegio.designsystem.component.GymBroTextButton
import com.pegio.designsystem.component.GymBroTextField
import com.pegio.feed.R
import com.pegio.feed.presentation.component.PostImage
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

    val launchGallery = rememberGalleryLauncher(
        onImageSelected = { viewModel.onEvent(CreatePostUiEvent.OnPhotoSelected(it)) }
    )

    SetupTopBar(viewModel.uiState.isLoading, onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Main
            CreatePostUiEffect.LaunchGallery -> launchGallery.invoke()

            // Failure
            is CreatePostUiEffect.ShowSnackbar ->
                onShowSnackbar(context.getString(effect.errorRes), null)

            // Navigation
            CreatePostUiEffect.NavigateBack -> onDismiss.invoke()
        }
    }

    CreatePostContent(state = viewModel.uiState, onEvent = viewModel::onEvent)

    if (viewModel.uiState.isLoading) LoadingIndicatorContent()
}

@Composable
private fun CreatePostContent(
    state: CreatePostUiState,
    onEvent: (CreatePostUiEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalDivider()

        CreatePostActions(
            enabled = !state.isLoading,
            onGalleryClick = { onEvent(CreatePostUiEvent.OnOpenGallery) },
            onDumbbellClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(horizontal = 16.dp)
        )

        GymBroTextField(
            value = state.postText,
            enabled = !state.isLoading,
            onValueChange = { onEvent(CreatePostUiEvent.OnPostTextChange(value = it)) },
            label = stringResource(R.string.feature_feed_what_s_on_your_muscle),
            modifier = Modifier
                .fillMaxWidth()
        )

        state.imageUri?.let { uri ->
            PostImage(
                imageUrl = uri.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
        }
    }
}

@Composable
private fun CreatePostActions(
    enabled: Boolean,
    onGalleryClick: () -> Unit,
    onDumbbellClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        GymBroIconButton(
            imageVector = Icons.Default.FitnessCenter,
            onClick = onDumbbellClick,
            enabled = enabled,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        GymBroTextButton(
            text = stringResource(R.string.feature_feed_choose_your_workout),
            onClick = onDumbbellClick,
            enabled = enabled
        )

        Spacer(modifier = Modifier.weight(1f))

        GymBroIconButton(
            imageVector = Icons.Default.Image,
            onClick = onGalleryClick,
            enabled = enabled,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun LoadingIndicatorContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun SetupTopBar(
    isLoading: Boolean,
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
                        onClick = { if (!isLoading) onEvent(CreatePostUiEvent.OnPostClick) }
                    )
                )
            )
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Preview(showBackground = true)
@Composable
private fun CreatePostContentPreview() {
    CreatePostContent(state = CreatePostUiState(), onEvent = { })
}