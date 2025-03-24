package com.pegio.gymbro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pegio.gymbro.presentation.auth.AuthScreen
import com.pegio.gymbro.presentation.core.Route
import com.pegio.gymbro.presentation.drawer.AppDrawer
import com.pegio.gymbro.presentation.home.HomeScreen
import com.pegio.gymbro.presentation.register.RegisterScreen
import com.pegio.gymbro.presentation.splash.SplashScreen
import com.pegio.gymbro.presentation.theme.GymBroTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymBroTheme {
                AppContent()
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun AppContent() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showTopBar = currentRoute !in listOf(
        Route.SplashScreen::class.qualifiedName,
        Route.AuthScreen::class.qualifiedName,
        Route.RegisterScreen::class.qualifiedName
    )

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        gestureEnabled = showTopBar
    ) {
        Scaffold(
            topBar = { if (showTopBar) TopAppBarContent(coroutineScope, drawerState) },
            content = { innerPadding ->
                NavigationHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun TopAppBarContent(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        title = { Text(text = "GymBro") },
        navigationIcon = {
            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Drawer"
                )
            }
        }
    )
}

@Composable
private fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Route.SplashScreen,
        modifier = modifier
    ) {
        composable<Route.SplashScreen> {
            SplashScreen(navController)
        }

        composable<Route.AuthScreen> {
            AuthScreen(navController)
        }

        composable<Route.RegisterScreen> {
            RegisterScreen(navController)
        }

        composable<Route.HomeScreen> {
            HomeScreen()
        }
    }
}