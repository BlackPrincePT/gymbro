package com.pegio.feed.presentation.screen.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.component.CreatePost
import com.pegio.feed.presentation.component.PostContent
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.feed.presentation.screen.feed.state.FeedUiEffect
import com.pegio.feed.presentation.screen.feed.state.FeedUiEvent
import com.pegio.feed.presentation.screen.feed.state.FeedUiState

@Composable
internal fun FeedScreen(
    onCreatePostClick: () -> Unit,
    onChatClick: () -> Unit,
    onOpenDrawerClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            FeedUiEffect.NavigateToCreatePost -> onCreatePostClick()

            // Top Bar
            FeedUiEffect.OpenDrawer -> onOpenDrawerClick()
            FeedUiEffect.NavigateToChat -> onChatClick()
        }
    }

    FeedContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun FeedContent(
    state: FeedUiState,
    onEvent: (FeedUiEvent) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            CreatePost(
                currentUser = state.currentUser,
                onPostClick = { onEvent(FeedUiEvent.OnCreatePostClick) },
                onProfileClick = { }
            )
        }

        // TODO: Add stories

        items(state.relevantPosts) {
            PostContent(
                post = it,
                onUpVoteClick = { },
                onDownVoteClick = { },
                onCommentClick = { },
                onRatingClick = { }
            )
        }

        if (state.isLoading)
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
    }
}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (FeedUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.Default.Menu,
                    onClick = { onEvent(FeedUiEvent.OnDrawerClick) }
                ),
                actions = listOf(
                    TopBarAction(
                        icon = Icons.Default.ChatBubble,
                        onClick = { onEvent(FeedUiEvent.OnChatClick) }
                    )
                )
            )
        )
    }
}

@Preview
@Composable
private fun FeedContentPreview() {
    FeedContent(state = FeedUiState(), onEvent = { })
}