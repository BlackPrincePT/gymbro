package com.pegio.gymbro.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.gymbro.presentation.components.AppDrawerContent
import com.pegio.gymbro.presentation.components.CreatePost
import com.pegio.gymbro.presentation.components.PostContent
import com.pegio.gymbro.presentation.components.TopAppBarContent
import com.pegio.gymbro.presentation.screen.ai_chat.AiChatUiEvent
import com.pegio.gymbro.presentation.theme.GymBroTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onChatClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    onAccountClick: () -> Unit,
    onSignOutSuccess: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                HomeUiEffect.SignedOutSuccessfully -> onSignOutSuccess()
                HomeUiEffect.NavigateToChat -> onChatClick()
                HomeUiEffect.NavigateToCreatePost -> onCreatePostClick()
                HomeUiEffect.NavigateToAccount -> {
                    onAccountClick()
                    drawerState.close()
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                displayedUser = viewModel.uiState.currentUser,
                onAccountClick = { viewModel.onEvent(HomeUiEvent.OnAccountClick) },
                onSignOutClick = { viewModel.onEvent(HomeUiEvent.OnSignOut) }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBarContent(
                    drawerState,
                    onChatClick = { viewModel.onEvent(HomeUiEvent.OnChatClick) })
            },
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->

            HomeContent(
                state = viewModel.uiState,
                onEvent = viewModel::onEvent,
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
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

@Preview
@Composable
private fun HomeContentPreview() {
    GymBroTheme {
        HomeContent(state = HomeUiState(), onEvent = { })
    }
}