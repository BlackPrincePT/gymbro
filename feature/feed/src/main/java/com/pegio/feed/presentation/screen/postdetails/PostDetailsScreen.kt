package com.pegio.feed.presentation.screen.postdetails

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEffect
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEvent
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiState

@Composable
internal fun PostDetailsScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: PostDetailsViewModel = hiltViewModel()
) {
    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            PostDetailsUiEffect.NavigateBack -> onBackClick()
        }
    }

    PostDetailsContent(viewModel.uiState, viewModel::onEvent)
}

@Composable
private fun PostDetailsContent(
    state: PostDetailsUiState,
    onEvent: (PostDetailsUiEvent) -> Unit
) {

}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (PostDetailsUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(PostDetailsUiEvent.OnBackClick) }
                )
            )
        )
    }
}

@Preview
@Composable
private fun PostDetailsContentPreview() {
    PostDetailsContent(state = PostDetailsUiState(), onEvent = { })
}