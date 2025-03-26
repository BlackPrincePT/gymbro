package com.pegio.gymbro.presentation.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pegio.gymbro.presentation.core.Route
import com.pegio.gymbro.presentation.theme.GymBroTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
@ExperimentalMaterial3Api
fun AppDrawer(
    navController: NavController,
    drawerState: DrawerState,
    gestureEnabled: Boolean,
    viewModel: AppDrawerViewModel = hiltViewModel(),
    content: @Composable () -> Unit = {}
) {
    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                AppDrawerUiEffect.NavigateToAuth -> navController.navigate(Route.AuthScreen)
                AppDrawerUiEffect.NavigateToAccount -> navController.navigate(Route.AccountScreen)
            }
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gestureEnabled,
        drawerContent = { AppDrawerContent(state = uiState, onEvent = viewModel::onEvent) },
        content = content
    )
}

@Composable
private fun AppDrawerContent(
    state: AppDrawerUiState,
    onEvent: (AppDrawerUiEvent) -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            NavigationDrawerItem(
                label = { Text(text = "Account") },
                selected = false,
                onClick = { onEvent(AppDrawerUiEvent.OnAccountClicked) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            Spacer(modifier = Modifier.weight(1f))

            NavigationDrawerItem(
                label = { Text(text = "Sign out", color = MaterialTheme.colorScheme.error) },
                selected = false,
                onClick = { onEvent(AppDrawerUiEvent.OnSignOut) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Preview
@Composable
private fun AppDrawerContentPreview() {
    GymBroTheme {
        AppDrawerContent(state = AppDrawerUiState(), onEvent = {})
    }
}