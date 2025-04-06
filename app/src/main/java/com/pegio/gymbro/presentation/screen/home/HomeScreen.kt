package com.pegio.gymbro.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.gymbro.presentation.activity.TopBarAction
import com.pegio.gymbro.presentation.activity.TopBarState
import com.pegio.gymbro.presentation.activity.components.AppDrawerContent
import com.pegio.gymbro.presentation.components.CreatePost
import com.pegio.gymbro.presentation.components.PostContent
import com.pegio.gymbro.presentation.core.theme.GymBroTheme
import com.pegio.gymbro.presentation.util.CollectLatestEffect

@Composable
fun HomeScreen(
    onCreatePostClick: () -> Unit,
    onChatClick: () -> Unit,
    onDrawerClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            HomeUiEffect.NavigateToCreatePost -> onCreatePostClick()

            // Top Bar
            HomeUiEffect.OpenDrawer -> onDrawerClick()
            HomeUiEffect.NavigateToChat -> onChatClick()
        }
    }

    HomeContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            CreatePost(
                currentUser = state.currentUser,
                onPostClick = { onEvent(HomeUiEvent.OnCreatePostClick) },
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
    }
}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (HomeUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.Default.Menu,
                    onClick = { onEvent(HomeUiEvent.OnDrawerClick) }
                ),
                actions = listOf(
                    TopBarAction(
                        icon = Icons.Default.ChatBubble,
                        onClick = { onEvent(HomeUiEvent.OnChatClick) }
                    )
                )
            )
        )
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    GymBroTheme {
        HomeContent(state = HomeUiState(), onEvent = { })
    }
}