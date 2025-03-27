package com.pegio.gymbro.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pegio.gymbro.presentation.components.AppDrawerContent
import com.pegio.gymbro.presentation.components.TopAppBarContent
import com.pegio.gymbro.presentation.theme.GymBroTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onChatClick: () -> Unit,
    onAccountClick: () -> Unit,
    onSignOutSuccess: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                HomeUiEffect.SignedOutSuccessfully -> onSignOutSuccess()
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
                displayedUser = uiState.displayedUser,
                onAccountClick = { viewModel.onEvent(HomeUiEvent.OnAccountClick) },
                onSignOutClick = { viewModel.onEvent(HomeUiEvent.OnSignOut) }
            )
        }
    ) {
        Scaffold(
            topBar = { TopAppBarContent(drawerState, onChatClick) },
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->

            HomeContent(
                state = uiState,
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

}

@Preview
@Composable
private fun HomeContentPreview() {
    GymBroTheme {
        HomeContent(state = HomeUiState(), onEvent = { })
    }
}