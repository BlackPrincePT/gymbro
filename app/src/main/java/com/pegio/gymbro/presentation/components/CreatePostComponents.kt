package com.pegio.gymbro.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pegio.gymbro.presentation.model.UiUser

@Composable
fun CreatePost(
    currentUser: UiUser,
    onPostClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPostClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
        ) {
            ProfileImage(
                imageUrl = currentUser.imgProfileUrl,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .clickable { onProfileClick.invoke() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "What's on your muscles?",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Preview
@Composable
private fun CreatePostPreview() {
    CreatePost(
        currentUser = UiUser.DEFAULT,
        onPostClick = { },
        onProfileClick = { }
    )
}