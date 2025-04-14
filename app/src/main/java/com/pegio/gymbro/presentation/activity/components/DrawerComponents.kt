package com.pegio.gymbro.presentation.activity.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.pegio.designsystem.component.BackgroundImage
import com.pegio.designsystem.component.ProfileImage
import com.pegio.gymbro.R
import com.pegio.presentation.model.UiUser

@Composable
fun DrawerContent(
    onGoogleAuthClick: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.sign_in_with_google)) },
                selected = false,
                onClick = onGoogleAuthClick,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Composable
fun DrawerContent(
    displayedUser: UiUser,
    onAccountClick: () -> Unit,
    onWorkoutPlanClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    ModalDrawerSheet {

        AppDrawerHeader(
            user = displayedUser,
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.account)) },
                selected = false,
                onClick = onAccountClick,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.built_in_workout_plans)) },
                selected = false,
                onClick = onWorkoutPlanClick,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            Spacer(modifier = Modifier.weight(1f))

            NavigationDrawerItem(
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
        }
    }
}

@Composable
private fun AppDrawerHeader(
    user: UiUser,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (backgroundImage, profileImage, username) = createRefs()

        BackgroundImage(
            imageUrl = user.imgBackgroundUrl,
            modifier = Modifier
                .height(96.dp)
                .constrainAs(backgroundImage) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                }
        )

        ProfileImage(
            imageUrl = user.imgProfileUrl,
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .zIndex(1f)
                .constrainAs(profileImage) {
                    top.linkTo(backgroundImage.bottom)
                    bottom.linkTo(backgroundImage.bottom)
                    centerHorizontallyTo(parent)
                }
        )

        Text(
            text = user.username,
            modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(username) {
                    top.linkTo(profileImage.bottom)
                    centerHorizontallyTo(parent)
                }
        )
    }
}

@Preview
@Composable
private fun AppDrawerContentPreview() {
    DrawerContent(
        displayedUser = UiUser.DEFAULT,
        onAccountClick = {},
        onWorkoutPlanClick = {},
        onSignOutClick = {}
    )
}