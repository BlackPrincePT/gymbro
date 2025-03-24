package com.pegio.gymbro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pegio.gymbro.presentation.ai_chat.AiChatScreen
import com.pegio.gymbro.presentation.auth.AuthScreen
import com.pegio.gymbro.presentation.core.Route
import com.pegio.gymbro.presentation.home.HomeScreen
import com.pegio.gymbro.presentation.register.RegisterScreen
import com.pegio.gymbro.presentation.theme.GymBroTheme
import com.pegio.gymbro.presentation.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymBroTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Route.AiChatScreen) {
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

                    composable<Route.AiChatScreen> {
                        AiChatScreen()
                    }


                }
            }
        }
    }
}