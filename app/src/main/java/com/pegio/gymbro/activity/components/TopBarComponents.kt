package com.pegio.gymbro.activity.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.pegio.gymbro.activity.TopBarState

@Composable
@ExperimentalMaterial3Api
fun TopBarContent(topBarState: TopBarState) {
    TopAppBar(
        title = { Text(text = topBarState.title) },
        navigationIcon = {
            topBarState.navigationIcon?.let { action ->
                IconButton(onClick = action.onClick) {
                    Icon(imageVector = action.icon, contentDescription = null)
                }
            }
        },
        actions = {
            topBarState.actions.forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(imageVector = action.icon, contentDescription = null)
                }
            }
        }
    )
}