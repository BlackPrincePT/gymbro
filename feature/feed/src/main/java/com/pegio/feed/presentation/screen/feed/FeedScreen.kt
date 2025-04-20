package com.pegio.feed.presentation.screen.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.PagingColumn
import com.pegio.feed.presentation.component.CreatePost
import com.pegio.feed.presentation.component.PostContent
import com.pegio.feed.presentation.screen.feed.state.FeedUiEffect
import com.pegio.feed.presentation.screen.feed.state.FeedUiEvent
import com.pegio.feed.presentation.screen.feed.state.FeedUiState

@Composable
internal fun FeedScreen(
    onCreatePostClick: () -> Unit,
    onChatClick: () -> Unit,
    onShowPostDetails: (String) -> Unit,
    onUserProfileClick: (String) -> Unit,
    onOpenDrawerClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Top Bar
            FeedUiEffect.OpenDrawer -> onOpenDrawerClick()
            FeedUiEffect.NavigateToChat -> onChatClick()

            // Navigation
            FeedUiEffect.NavigateToCreatePost -> onCreatePostClick()
            is FeedUiEffect.NavigateToPostDetails -> onShowPostDetails(effect.postId)
            is FeedUiEffect.NavigateToUserProfile -> onUserProfileClick(effect.userId)
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
    PagingColumn(
        itemCount = state.relevantPosts.size,
        isLoading = state.isLoading,
        onLoadAnotherPage = { onEvent(FeedUiEvent.OnLoadMorePosts) },
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            CreatePost(
                currentUser = state.currentUser,
                onPostClick = { onEvent(FeedUiEvent.OnCreatePostClick) },
                onProfileClick = { onEvent(FeedUiEvent.OnUserProfileClick(userId = state.currentUser.id)) },
                modifier = Modifier
                    .padding(8.dp)
            )
        }

        // TODO: Add stories

        items(state.relevantPosts) { post ->
            PostContent(
                post = post,
                onProfileClick = { onEvent(FeedUiEvent.OnUserProfileClick(userId = post.author.id)) },
                onUpVoteClick = { },
                onDownVoteClick = { },
                onCommentClick = { onEvent(FeedUiEvent.OnPostCommentClick(postId = post.id)) },
                onRatingClick = { }
            )
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

@Preview(showBackground = true)
@Composable
private fun FeedContentPreview() {
    FeedContent(state = FeedUiState(), onEvent = { })
}