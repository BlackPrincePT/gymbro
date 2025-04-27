package com.pegio.gymbro.activity.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.pegio.common.presentation.components.BackgroundImage
import com.pegio.common.presentation.components.ProfileImage
import com.pegio.common.presentation.model.UiUser
import com.pegio.gymbro.R
import com.pegio.common.R as cR

@Composable
fun DrawerContent(
    isAnonymous: Boolean,
    username: String?,
    backgroundUrl: String?,
    avatarUrl: String?,
    onAvatarClick: () -> Unit,
    onWorkoutPlanClick: () -> Unit,
    onAccountClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onGoogleAuthClick: () -> Unit,
) {
    ModalDrawerSheet {

        if (!isAnonymous) AppDrawerHeader(
            backgroundUrl = backgroundUrl,
            avatarUrl = avatarUrl,
            username = username,
            onAvatarClick = onAvatarClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.built_in_workout_plans)) },
            selected = false,
            onClick = onWorkoutPlanClick,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        if (!isAnonymous) NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.account)) },
            selected = false,
            onClick = onAccountClick,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.settings)) },
            selected = false,
            onClick = onSettingsClick,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.weight(1f))

        if (!isAnonymous) NavigationDrawerItem(
            label = {
                Text(
                    text = stringResource(R.string.sign_out),
                    color = MaterialTheme.colorScheme.error
                )
            },
            selected = false,
            onClick = onSignOutClick,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        else NavigationDrawerItem(
            label = { Text(text = stringResource(cR.string.feature_common_sign_in_with_google)) },
            icon = {
                Icon(
                    painter = painterResource(id = cR.drawable.feature_common_ic_google_logo),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
            },
            selected = false,
            onClick = onGoogleAuthClick,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
private fun AppDrawerHeader(
    username: String?,
    avatarUrl: String?,
    backgroundUrl: String?,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box {
            BackgroundImage(
                imageUrl = backgroundUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            )

            ProfileImage(
                imageUrl = avatarUrl,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 32.dp)
                    .clip(CircleShape)
                    .clickable { onAvatarClick.invoke() }
                    .size(64.dp)
                    .zIndex(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        username?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun DefaultDrawerContentPreview() = with(UiUser.DEFAULT) {
    DrawerContent(
        isAnonymous = false,
        username = username,
        avatarUrl = avatarUrl,
        backgroundUrl = imgBackgroundUrl,
        onAvatarClick = { },
        onAccountClick = { },
        onWorkoutPlanClick = { },
        onSettingsClick = { },
        onSignOutClick = { },
        onGoogleAuthClick = { }
    )
}

@Preview
@Composable
private fun AnonymousDrawerContentPreview() {
    DrawerContent(
        isAnonymous = true,
        username = null,
        avatarUrl = null,
        backgroundUrl = null,
        onAvatarClick = { },
        onAccountClick = { },
        onWorkoutPlanClick = { },
        onSettingsClick = { },
        onSignOutClick = { },
        onGoogleAuthClick = { }
    )
}