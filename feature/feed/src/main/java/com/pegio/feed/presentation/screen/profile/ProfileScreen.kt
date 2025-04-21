package com.pegio.feed.presentation.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.PagingColumn
import com.pegio.designsystem.component.BackgroundImage
import com.pegio.designsystem.component.ProfileImage
import com.pegio.feed.R
import com.pegio.feed.presentation.component.CreatePost
import com.pegio.feed.presentation.component.PostContent
import com.pegio.feed.presentation.model.UiPost
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEffect
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEvent
import com.pegio.feed.presentation.screen.profile.state.ProfileUiState

@Composable
internal fun ProfileScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Navigation
            ProfileUiEffect.NavigateBack -> onBackClick()
        }
    }

    ProfileContent(state = viewModel.uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {
    PagingColumn(
        itemCount = state.userPosts.size,
        isLoading = state.isLoading,
        onLoadAnotherPage = { onEvent(ProfileUiEvent.OnLoadMorePosts) },
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            ProfileHeader(
                user = state.displayedUser,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
            if (state.isProfileOwner) {
                ProfileOwnerActions()
            } else {
                DefaultProfileActions(
                    isFollowing = state.isFollowing,
                    onFollowClick = { onEvent(ProfileUiEvent.OnFollowClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                )
            }
        }

        if (state.isProfileOwner) item {
            CreatePost(
                currentUser = state.displayedUser,
                onPostClick = { },
                onProfileClick = { }
            )
        }

        items(state.userPosts) { post ->
            PostContent(
                post = post,
                onProfileClick = { },
                onVoteClick = { onEvent(ProfileUiEvent.OnPostVote(post.id, voteType = it)) },
                onCommentClick = { },
                onRatingClick = { }
            )
        }
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun ProfileHeader(
    user: UiUser,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            BackgroundImage(
                imageUrl = user.imgBackgroundUrl,
                modifier = Modifier
                    .height(128.dp)
                    .fillMaxWidth()
            )

            ProfileImage(
                imageUrl = user.avatarUrl,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 64.dp)
                    .clip(CircleShape)
                    .size(128.dp)
                    .zIndex(1f)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = user.username,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Composable
private fun ProfileOwnerActions(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 8.dp)
        ) {
            Text(text = "Add to story")
        }

        Button(
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 16.dp)
        ) {
            Text(text = "Edit Profile")
        }
    }
}

@Composable
private fun DefaultProfileActions(
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onFollowClick,
        modifier = modifier
    ) {
        val btnText =
            if (isFollowing) stringResource(R.string.feature_feed_following)
            else stringResource(R.string.feature_feed_follow)

        Text(text = btnText)
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (ProfileUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(ProfileUiEvent.OnBackClick) }
                )
            )
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Preview(showBackground = true)
@Composable
private fun ProfileContentPreview() {
    val uiState = ProfileUiState(
        displayedUser = UiUser.DEFAULT,
        userPosts = List(size = 3) { UiPost.DEFAULT }
    )

    ProfileContent(state = uiState, onEvent = { })
}

@Preview
@Composable
private fun ProfileHeaderPreview() {
    ProfileHeader(user = UiUser.DEFAULT)
}