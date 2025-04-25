package com.pegio.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GymBroTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        elevation = elevation,
        border = border,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier

    ) {
        Text(text = text)
    }
}