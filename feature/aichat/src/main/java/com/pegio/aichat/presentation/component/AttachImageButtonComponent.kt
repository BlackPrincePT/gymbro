package com.pegio.aichat.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pegio.aichat.R

@Composable
fun AttachImageButton(
    onImageSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onImageSelect, modifier = Modifier.padding(4.dp)) {
        Icon(
            imageVector = Icons.Default.AttachFile,
            contentDescription = stringResource(R.string.feature_aichat_attach_image),
            tint = Color.Black,
            modifier = modifier.size(28.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAttachImageButton() {
    AttachImageButton(
        onImageSelect = {}
    )
}