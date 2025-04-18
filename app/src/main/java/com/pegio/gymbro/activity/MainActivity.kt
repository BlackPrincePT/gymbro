package com.pegio.gymbro.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pegio.auth.presentation.screen.auth.navigation.AuthRoute
import com.pegio.auth.presentation.screen.auth.navigation.navigateToAuth
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.theme.GymBroTheme
import com.pegio.gymbro.activity.components.DrawerContent
import com.pegio.gymbro.activity.components.TopBarContent
import com.pegio.gymbro.activity.state.MainActivityUiEffect
import com.pegio.gymbro.activity.state.MainActivityUiEvent
import com.pegio.gymbro.activity.state.MainActivityUiState
import com.pegio.gymbro.navigation.NavigationHost
import com.pegio.gymbro.navigation.route.RegisterRoute
import com.pegio.gymbro.navigation.route.SplashRoute
import com.pegio.gymbro.navigation.route.navigateToAccount
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
                val navController = rememberNavController()

                val snackbarHostState = remember { SnackbarHostState() }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val isEntryScreenDisplayed = currentRoute in listOf(
                    SplashRoute::class.qualifiedName,
                    AuthRoute::class.qualifiedName,
                    RegisterRoute::class.qualifiedName
                )

                CollectLatestEffect(viewModel.uiEffect) { effect ->
                    when (effect) {

                        // Open-Close Drawer
                        MainActivityUiEffect.OpenDrawer -> coroutineScope.launch { drawerState.open() }
                        MainActivityUiEffect.CloseDrawer -> coroutineScope.launch { drawerState.close() }

                        // Drawer
                        MainActivityUiEffect.NavigateToAccount -> navController.navigateToAccount()
                        MainActivityUiEffect.NavigateToWorkoutPlan -> navController.navigateToWorkoutPlan()
                        MainActivityUiEffect.NavigateToAuth -> navController.navigateToAuth()
                    }
                }

                AppContent(
                    state = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    drawerState = drawerState,
                    isEntryScreenDisplayed = isEntryScreenDisplayed
                )
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun AppContent(
    state: MainActivityUiState,
    onEvent: (MainActivityUiEvent) -> Unit,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    isEntryScreenDisplayed: Boolean
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isEntryScreenDisplayed.not(),
        drawerContent = {
            state.currentUser?.let {
                DrawerContent(
                    displayedUser = it,
                    onAccountClick = { onEvent(MainActivityUiEvent.OnAccountClick) },
                    onWorkoutPlanClick = { onEvent(MainActivityUiEvent.OnWorkoutPlanClick) },
                    onSignOutClick = { onEvent(MainActivityUiEvent.OnSignOutClick) }
                )
            } ?: DrawerContent(onGoogleAuthClick = { })
        }
    ) {
        Scaffold(
            topBar = { if (isEntryScreenDisplayed.not()) TopBarContent(state.topBarState) },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                )
            },
        ) { innerPadding ->
            NavigationHost(
                navController = navController,
                onSetupAppBar = { onEvent(MainActivityUiEvent.OnUpdateTopBarState(it)) },
                dynamicallyOpenDrawer = { onEvent(MainActivityUiEvent.OnOpenDrawer) },
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = Short
                    ) == ActionPerformed
                },
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}