package com.pegio.common.presentation.state

import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarAction(
    val icon: ImageVector,
    val onClick: () -> Unit
)