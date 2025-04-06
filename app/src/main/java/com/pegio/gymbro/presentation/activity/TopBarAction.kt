package com.pegio.gymbro.presentation.activity

import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarAction(
    val icon: ImageVector,
    val onClick: () -> Unit
)