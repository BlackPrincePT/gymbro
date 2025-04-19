package com.pegio.aichat.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SendButton(
    text: String,
    onSend: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onSend,
        modifier = modifier.padding(10.dp),
        enabled = text.isNotBlank() && !isLoading
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.Send,
            contentDescription = null,
            tint = when {
                isLoading -> Color.Gray
                text.isNotBlank() -> Color.Black
                else -> Color.Gray
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSendButton() {
    SendButton(
        text = "text",
        onSend = {},
        isLoading = false,
    )
}
