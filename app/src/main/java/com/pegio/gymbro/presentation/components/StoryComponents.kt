package com.pegio.gymbro.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun Story(
    userProfileUrl: String?,
    storyDisplayUrl: String?
) {
    Box(
        modifier = Modifier
            .size(width = 120.dp, height = 180.dp)
    ) {
        ProfileImage(
            imageUrl = userProfileUrl,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 4.dp, start = 4.dp)
                .clip(CircleShape)
                .size(24.dp)
        )

        AsyncImage(
            model = storyDisplayUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun StoryPreview() {
    Story(
        userProfileUrl = null,
        storyDisplayUrl = null
    )
}