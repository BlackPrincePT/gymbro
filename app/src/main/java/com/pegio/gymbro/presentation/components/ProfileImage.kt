package com.pegio.gymbro.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.pegio.gymbro.R

@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Profile picture",
        placeholder = painterResource(R.drawable.ic_launcher_background), // TODO
        error = painterResource(R.drawable.ic_launcher_background), // TODO
        modifier = modifier
    )
}