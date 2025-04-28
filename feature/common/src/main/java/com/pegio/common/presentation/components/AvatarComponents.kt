package com.pegio.common.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.pegio.common.R
import com.pegio.designsystem.component.GymBroTextButton

@Composable
fun EditAvatarContent(
    imageUrl: String?,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ProfileAvatar(
            imageUrl = imageUrl,
            isLoading = isLoading,
            onClick = onClick,
            isCameraIconVisible = true,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClick.invoke() }
                .size(96.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        GymBroTextButton(
            text = stringResource(R.string.feature_common_edit_your_profile_picture),
            onClick = onClick
        )
    }
}

@Composable
fun ProfileAvatar(
    imageUrl: String?,
    isLoading: Boolean,
    onClick: () -> Unit,
    isCameraIconVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { if (isCameraIconVisible) onClick() }
    ) {
        if (isCameraIconVisible) CameraIcon(
            isLoading = isLoading,
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
                .size(24.dp)
                .zIndex(1f)
        )

        ProfileImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CameraIcon(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        isLoading -> {
            CircularProgressIndicator(
                color = Color.White,
                modifier = modifier
            )
        }

        else -> {
            IconButton(
                onClick = onClick,
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}