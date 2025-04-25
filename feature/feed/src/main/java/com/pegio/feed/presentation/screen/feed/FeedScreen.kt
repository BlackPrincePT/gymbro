package com.pegio.feed.presentation.screen.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.PagingColumn
import com.pegio.feed.presentation.component.CreatePostContent
import com.pegio.feed.presentation.component.PostContent
import com.pegio.feed.presentation.model.UiPost
import com.pegio.feed.presentation.screen.feed.state.FeedUiEffect
import com.pegio.feed.presentation.screen.feed.state.FeedUiEvent
import com.pegio.feed.presentation.screen.feed.state.FeedUiState

@Composable
internal fun FeedScreen(
    onCreatePostClick: (Boolean) -> Unit,
    onChatClick: () -> Unit,
    onShowPostDetails: (String) -> Unit,
    onUserProfileClick: (String) -> Unit,
    onOpenDrawerClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Failure
            is FeedUiEffect.ShowSnackbar -> onShowSnackbar(context.getString(effect.errorRes))

            // Navigation
            FeedUiEffect.OpenDrawer -> onOpenDrawerClick()
            FeedUiEffect.NavigateToChat -> onChatClick()
            is FeedUiEffect.NavigateToCreatePost -> onCreatePostClick(effect.shouldOpenGallery)
            is FeedUiEffect.NavigateToPostDetails -> onShowPostDetails(effect.postId)
            is FeedUiEffect.NavigateToUserProfile -> onUserProfileClick(effect.userId)
        }
    }

    FeedContent(state = viewModel.uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun FeedContent(
    state: FeedUiState,
    onEvent: (FeedUiEvent) -> Unit
) = with(state) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { onEvent(FeedUiEvent.OnPostsRefresh) }
    ) {
        PagingColumn(
            itemCount = relevantPosts.size,
            isLoading = isLoading,
            onLoadAnotherPage = { onEvent(FeedUiEvent.OnLoadMorePosts) },
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            item {
                CreatePostContent(
                    currentUser = currentUser,
                    onClick = { onEvent(FeedUiEvent.OnCreatePostClick(it)) },
                    onProfileClick = { onEvent(FeedUiEvent.OnUserProfileClick(userId = currentUser.id)) },
                    modifier = Modifier
                        .padding(16.dp)
                )
            }

            items(relevantPosts) { post ->
                PostContent(
                    post = post,
                    onProfileClick = { onEvent(FeedUiEvent.OnUserProfileClick(userId = post.author.id)) },
                    onCommentClick = { onEvent(FeedUiEvent.OnPostCommentClick(postId = post.id)) },
                    onVoteClick = {
                        onEvent(FeedUiEvent.OnPostVote(postId = post.id, voteType = it))
                    }
                )
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

@Preview(showBackground = true)
@Composable
private fun FeedContentPreview() {
    FeedContent(
        state = FeedUiState(relevantPosts = List(5) { UiPost.DEFAULT }),
        onEvent = { }
    )
}