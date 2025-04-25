package com.pegio.feed.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.components.BackgroundImage
import com.pegio.common.presentation.components.LoadingItemsIndicator
import com.pegio.common.presentation.components.ProfileImage
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.PagingColumn
import com.pegio.common.presentation.util.rememberGalleryLauncher
import com.pegio.feed.R
import com.pegio.feed.presentation.component.CreatePostContent
import com.pegio.feed.presentation.component.PostContent
import com.pegio.feed.presentation.model.UiPost
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEffect
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEvent
import com.pegio.feed.presentation.screen.profile.state.ProfileUiState
import com.pegio.model.FollowRecord
import kotlinx.coroutines.launch

@Composable
internal fun ProfileScreen(
    onBackClick: () -> Unit,
    onFollowRecordClick: (String, FollowRecord.Type) -> Unit,
    onCreatePostClick: (Boolean) -> Unit,
    onShowPostDetails: (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val launchGallery = rememberGalleryLauncher(
        onImageSelected = {
            viewModel.editMode?.run { viewModel.onEvent(getUploadEvent(it)) }
        }
    )

    SetupTopBar(viewModel.uiState.displayedUser.username, onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Image
            is ProfileUiEffect.LaunchGallery -> launchGallery.invoke()

            // Failure
            is ProfileUiEffect.ShowSnackbar -> onShowSnackbar(context.getString(effect.errorRes))

            // Navigation
            ProfileUiEffect.NavigateBack -> onBackClick()
            is ProfileUiEffect.NavigateToCreatePost -> onCreatePostClick(effect.shouldOpenGallery)
            is ProfileUiEffect.NavigateToPostDetail -> onShowPostDetails(effect.postId)
            is ProfileUiEffect.NavigateToFollowRecord ->
                onFollowRecordClick(effect.userId, effect.mode)
        }
    }

    ProfileContent(state = viewModel.uiState, onEvent = viewModel::onEvent)

    if (viewModel.uiState.shouldShowBottomSheet)
        BottomSheetContent(mode = viewModel.editMode, onEvent = viewModel::onEvent)
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) = with(state) {
    PagingColumn(
        itemCount = userPosts.size,
        isLoading = isLoading,
        onLoadAnotherPage = { onEvent(ProfileUiEvent.OnLoadMorePosts) },
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        item {
            ProfileHeader(
                user = displayedUser,
                isProfileOwner = isProfileOwner,
                isLoadingAvatar = isAvatarLoading,
                isLoadingBackground = isBackgroundLoading,
                onCameraIconClick = { onEvent(ProfileUiEvent.OnBottomSheetStateUpdate(shouldShow = true)) },
                onFollowRecordClick = {
                    onEvent(ProfileUiEvent.OnFollowRecordClick(displayedUser.id, it))
                },
                onEditModeChange = { onEvent(ProfileUiEvent.OnEditModeChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        if (!isProfileOwner) item {
            DefaultProfileActions(
                isFollowing = isFollowing,
                onFollowClick = { onEvent(ProfileUiEvent.OnFollowClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
        }

        if (isProfileOwner) item {
            CreatePostContent(
                onClick = { onEvent(ProfileUiEvent.OnCreatePostClick(it)) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }

        if (userPosts.isEmpty() && !isLoading) item {
            EmptyUsersContent(
                username = displayedUser.username,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
        else items(userPosts) { post ->
            PostContent(
                post = post,
                onVoteClick = { onEvent(ProfileUiEvent.OnPostVote(post.id, voteType = it)) },
                onCommentClick = { onEvent(ProfileUiEvent.OnPostCommentClick(post.id)) }
            )
        }

        if (isLoading)
            item { LoadingItemsIndicator() }
    }
}

@Composable
private fun EmptyUsersContent(
    username: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.feature_feed_has_not_posted_yet, username),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        color = Color.Gray,
        modifier = modifier
    )
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun ProfileHeader(
    user: UiUser,
    isProfileOwner: Boolean,
    isLoadingAvatar: Boolean,
    isLoadingBackground: Boolean,
    onCameraIconClick: () -> Unit,
    onFollowRecordClick: (FollowRecord.Type) -> Unit,
    onEditModeChange: (ProfileEditMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            ProfileBackground(
                imageUrl = user.imgBackgroundUrl,
                isProfileOwner = isProfileOwner,
                isLoading = isLoadingBackground,
                onCameraIconClick = {
                    onEditModeChange(ProfileEditMode.Background)
                    onCameraIconClick()
                },
                modifier = Modifier
                    .height(128.dp)
                    .fillMaxWidth()
            )

            ProfileAvatar(
                imageUrl = user.avatarUrl,
                isProfileOwner = isProfileOwner,
                isLoading = isLoadingAvatar,
                onCameraIconClick = {
                    onEditModeChange(ProfileEditMode.Avatar)
                    onCameraIconClick()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 64.dp)
                    .clip(CircleShape)
                    .size(128.dp)
                    .zIndex(1f)
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = user.username,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        ProfileFollowRecords(
            followersCount = user.followersCount,
            followingCount = user.followingCount,
            onFollowersClick = { onFollowRecordClick(FollowRecord.Type.FOLLOWERS) },
            onFollowingClick = { onFollowRecordClick(FollowRecord.Type.FOLLOWING) }
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


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
        val buttonText =
            if (isFollowing) stringResource(R.string.feature_feed_following)
            else stringResource(R.string.feature_feed_follow)

        Text(text = buttonText)
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun ProfileFollowRecords(
    followersCount: Int,
    followingCount: Int,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FollowRecordItem(
            count = followersCount,
            label = stringResource(R.string.feature_feed_followers),
            onClick = onFollowersClick
        )

        FollowRecordItem(
            count = followingCount,
            label = stringResource(id = R.string.feature_feed_following),
            onClick = onFollowingClick
        )
    }
}

@Composable
private fun FollowRecordItem(
    count: Int,
    label: String,
    onClick: () -> Unit = { }
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun ProfileAvatar(
    imageUrl: String?,
    isProfileOwner: Boolean,
    isLoading: Boolean,
    onCameraIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clickable { onCameraIconClick() }) {
        CameraIcon(
            isProfileOwner = isProfileOwner,
            isLoading = isLoading,
            onClick = onCameraIconClick,
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
private fun ProfileBackground(
    imageUrl: String?,
    isProfileOwner: Boolean,
    isLoading: Boolean,
    onCameraIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clickable { onCameraIconClick() }) {
        CameraIcon(
            isProfileOwner = isProfileOwner,
            isLoading = isLoading,
            onClick = onCameraIconClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
                .zIndex(1f)
        )

        BackgroundImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CameraIcon(
    isProfileOwner: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        isProfileOwner && isLoading -> {
            CircularProgressIndicator(
                color = Color.White,
                modifier = modifier
            )
        }

        isProfileOwner -> {
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


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun BottomSheetContent(
    mode: ProfileEditMode?,
    onEvent: (ProfileUiEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val fnHideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible)
                onEvent(ProfileUiEvent.OnBottomSheetStateUpdate(shouldShow = false))
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onEvent(ProfileUiEvent.OnBottomSheetStateUpdate(shouldShow = false)) },
        sheetState = sheetState,
        dragHandle = null
    ) {
        Button(
            onClick = {
                onEvent(ProfileUiEvent.OnOpenGallery)
                fnHideSheet.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
        ) {
            Text(text = stringResource(R.string.feature_feed_choose_from_gallery))
        }

        Button(
            onClick = {
                mode?.run { onEvent(getRemoveEvent()) }
                fnHideSheet.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(text = stringResource(R.string.feature_feed_remove))
        }
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun SetupTopBar(
    title: String,
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (ProfileUiEvent) -> Unit
) {
    LaunchedEffect(title) {
        onSetupTopBar(
            TopBarState(
                title = title,
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
private fun ProfileHeaderPreview() {
    ProfileHeader(
        user = UiUser.DEFAULT,
        isProfileOwner = true,
        isLoadingAvatar = false,
        isLoadingBackground = false,
        onCameraIconClick = { },
        onFollowRecordClick = { },
        onEditModeChange = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileContentPreview() {
    val uiState = ProfileUiState(
        displayedUser = UiUser.DEFAULT,
        userPosts = List(size = 3) { UiPost.DEFAULT }
    )

    ProfileContent(state = uiState, onEvent = { })
}