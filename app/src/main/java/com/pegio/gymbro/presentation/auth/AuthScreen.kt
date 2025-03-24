package com.pegio.gymbro.presentation.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { viewModel.launchGoogleAuthOptions(context) },
            modifier = Modifier.align(alignment = Alignment.Center)
        ) {
            Text("Sign in with Google")
        }
    }
}
