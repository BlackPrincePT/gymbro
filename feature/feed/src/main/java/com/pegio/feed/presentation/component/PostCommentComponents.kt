package com.pegio.feed.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pegio.designsystem.component.ProfileImage

@Composable
internal fun PostCommentContent(
    avatarUrl: String?,
    username: String,
    commentText: String,
    commentDate: String,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        ProfileImage(
            imageUrl = avatarUrl,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable { onProfileClick.invoke() }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .padding(end = 48.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = username,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .clickable { onProfileClick.invoke() }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = commentText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = commentDate,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun WriteCommentContent(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        trailingIcon = {
            IconButton(onClick = onSubmit) {
                Icon(imageVector = Icons.AutoMirrored.Default.Send, contentDescription = null)
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun PostCommentContentPreview() {
    PostCommentContent(
        avatarUrl = null,
        username = "Pitiful Android Developer",
        commentText = "I believe in taking care of myself and a balanced diet and rigorous exercise routine.",
        commentDate = "2 hrs",
        onProfileClick = { }
    )
}

@Preview
@Composable
private fun WriteCommentContentPreview() {
    WriteCommentContent(
        value = "",
        onValueChange = { },
        label = "Write a comment...",
        onSubmit = { },
        modifier = Modifier
            .fillMaxWidth()
    )
}