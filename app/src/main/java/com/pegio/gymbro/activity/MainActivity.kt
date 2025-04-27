package com.pegio.gymbro.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pegio.auth.presentation.screen.auth.navigation.AuthRoute
import com.pegio.auth.presentation.screen.auth.navigation.navigateToAuth
import com.pegio.auth.presentation.screen.register.navigation.RegisterRoute
import com.pegio.auth.presentation.screen.register.navigation.navigateToRegister
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.theme.GymBroTheme
import com.pegio.feed.presentation.screen.profile.navigation.navigateToProfile
import com.pegio.gymbro.activity.components.DrawerContent
import com.pegio.gymbro.activity.components.TopBarContent
import com.pegio.gymbro.activity.state.MainActivityUiEffect
import com.pegio.gymbro.activity.state.MainActivityUiEvent
import com.pegio.gymbro.activity.state.MainActivityUiState
import com.pegio.gymbro.navigation.NavigationHost
import com.pegio.gymbro.navigation.route.navigateToWorkoutPlan
import com.pegio.settings.presentation.screen.account.navigation.navigateToAccount
import com.pegio.settings.presentation.screen.settings.navigation.navigateToSettings
import com.pegio.splash.presentation.splash.navigation.SplashRoute
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
            GymBroTheme(viewModel.uiState.themeMode) {
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

                RequestNotificationPermission()

                CollectLatestEffect(viewModel.uiEffect) { effect ->
                    when (effect) {

                        // Open-Close Drawer
                        MainActivityUiEffect.OpenDrawer -> coroutineScope.launch { drawerState.open() }
                        MainActivityUiEffect.CloseDrawer -> coroutineScope.launch { drawerState.close() }

                        // Navigation
                        MainActivityUiEffect.NavigateToAccount -> navController.navigateToAccount()
                        MainActivityUiEffect.NavigateToSettings -> navController.navigateToSettings()
                        MainActivityUiEffect.NavigateToWorkoutPlan -> navController.navigateToWorkoutPlan()
                        MainActivityUiEffect.NavigateToAuth -> navController.navigateToAuth()
                        MainActivityUiEffect.NavigateToRegister -> navController.navigateToRegister()
                        is MainActivityUiEffect.NavigateToProfile ->
                            navController.navigateToProfile(userId = effect.userId)
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
) = with(state) {

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isEntryScreenDisplayed,
        drawerContent = {
            DrawerContent(
                isAnonymous = isAnonymous,
                backgroundUrl = currentUser?.imgBackgroundUrl,
                avatarUrl = currentUser?.avatarUrl,
                username = currentUser?.username,
                onAvatarClick = { onEvent(MainActivityUiEvent.OnProfileClick) },
                onAccountClick = { onEvent(MainActivityUiEvent.OnAccountClick) },
                onSettingsClick = { onEvent(MainActivityUiEvent.OnSettingsClick) },
                onWorkoutPlanClick = { onEvent(MainActivityUiEvent.OnWorkoutPlanClick) },
                onSignOutClick = { onEvent(MainActivityUiEvent.OnSignOutClick) },
                onGoogleAuthClick = { onEvent(MainActivityUiEvent.LinkAnonymousAccount(context)) }
            )
        }
    ) {
        Scaffold(
            topBar = { if (!isEntryScreenDisplayed) TopBarContent(state.topBarState) },
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
                dynamicallyOpenDrawer = { onEvent(MainActivityUiEvent.OnOpenDrawerClick) },
                onShowSnackbar = { snackbarHostState.showSnackbar(message = it, duration = Short) },
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun RequestNotificationPermission() {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}