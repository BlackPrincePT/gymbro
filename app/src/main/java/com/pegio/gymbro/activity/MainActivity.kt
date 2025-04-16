package com.pegio.gymbro.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.theme.GymBroTheme
import com.pegio.gymbro.activity.components.DrawerContent
import com.pegio.gymbro.activity.components.TopBarContent
import com.pegio.gymbro.activity.state.MainActivityUiEffect
import com.pegio.gymbro.activity.state.MainActivityUiEvent
import com.pegio.gymbro.activity.state.MainActivityUiState
import com.pegio.gymbro.navigation.EntryNavigationHost
import com.pegio.gymbro.navigation.MainNavigationHost
import com.pegio.gymbro.navigation.route.navigateToAccount
import com.pegio.gymbro.navigation.route.navigateToAuth
import com.pegio.gymbro.navigation.route.navigateToWorkoutPlan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymBroTheme {
                val entryNavController = rememberNavController()
                val mainNavController = rememberNavController()

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()

                CollectLatestEffect(viewModel.uiEffect) { effect ->
                    when (effect) {

                        // Open-Close Drawer
                        MainActivityUiEffect.OpenDrawer -> coroutineScope.launch { drawerState.open() }
                        MainActivityUiEffect.CloseDrawer -> coroutineScope.launch { drawerState.close() }

                        // Drawer
                        MainActivityUiEffect.NavigateToAccount -> mainNavController.navigateToAccount()
                        MainActivityUiEffect.NavigateToWorkoutPlan -> mainNavController.navigateToWorkoutPlan()
                        MainActivityUiEffect.NavigateToAuth -> entryNavController.navigateToAuth()
                    }
                }

                EntryNavigationHost(entryNavController) {
                    MainAppContent(
                        state = viewModel.uiState,
                        onEvent = viewModel::onEvent,
                        navController = mainNavController,
                        drawerState = drawerState
                    )
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun MainAppContent(
    state: MainActivityUiState,
    onEvent: (MainActivityUiEvent) -> Unit,
    navController: NavHostController,
    drawerState: DrawerState
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            state.currentUser?.let {
                DrawerContent(
                    displayedUser = it,
                    onAccountClick = { onEvent(MainActivityUiEvent.OnAccountClick) },
                    onWorkoutPlanClick = {onEvent(MainActivityUiEvent.OnWorkoutPlanClick)},
                    onSignOutClick = { onEvent(MainActivityUiEvent.OnSignOutClick) }
                )
            } ?: DrawerContent(onGoogleAuthClick = { })
        }
    ) {
        Scaffold(
            topBar = { TopBarContent(state.topBarState) },
            snackbarHost = { /* TODO: Snackbar */ },
        ) { innerPadding ->
            MainNavigationHost(
                navController = navController,
                onSetupAppBar = { onEvent(MainActivityUiEvent.OnUpdateTopBarState(it)) },
                dynamicallyOpenDrawer = { onEvent(MainActivityUiEvent.OnOpenDrawer) },
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}