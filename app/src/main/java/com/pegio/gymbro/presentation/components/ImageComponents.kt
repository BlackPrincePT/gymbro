package com.pegio.gymbro.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.pegio.gymbro.R

@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.profile_picture),
        fallback = ColorPainter(Color.Gray),
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
fun BackgroundImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.background_photo),
        fallback = ColorPainter(Color.LightGray),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
    )
}