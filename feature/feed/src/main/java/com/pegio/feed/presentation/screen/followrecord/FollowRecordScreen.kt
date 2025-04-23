package com.pegio.feed.presentation.screen.followrecord

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.PagingColumn
import com.pegio.designsystem.component.ProfileImage
import com.pegio.feed.R
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiEffect
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiEvent
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiState
import com.pegio.model.FollowRecord

@Composable
internal fun FollowRecordScreen(
    onBackClick: () -> Unit,
    onUserProfileClick: (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: FollowRecordViewModel = hiltViewModel()
) {
    SetupTopBar(viewModel.currentMode, onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Navigation
            FollowRecordUiEffect.NavigateBack -> onBackClick()
            is FollowRecordUiEffect.NavigateToUserProfile -> onUserProfileClick(effect.userId)
        }
    }

    FollowRecordContent(state = viewModel.uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun FollowRecordContent(
    state: FollowRecordUiState,
    onEvent: (FollowRecordUiEvent) -> Unit
) {
    PagingColumn(
        itemCount = state.users.size,
        isLoading = state.isLoading,
        onLoadAnotherPage = { onEvent(FollowRecordUiEvent.OnLoadMoreUsers) },
        loadIndex = 5
    ) {
        items(state.users) { user ->
            FollowRecordItem(
                user = user,
                onClick = { onEvent(FollowRecordUiEvent.OnUserProfileClick(user.id)) }
            )
        }
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun FollowRecordItem(
    user: UiUser,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(text = user.username) },
        leadingContent = {
            ProfileImage(
                imageUrl = user.avatarUrl,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
            )
        },
        modifier = modifier
            .clickable { onClick.invoke() }
    )
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun SetupTopBar(
    mode: FollowRecord.Type,
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (FollowRecordUiEvent) -> Unit
) {
    val title = when (mode) {
        FollowRecord.Type.FOLLOWERS -> stringResource(id = R.string.feature_feed_followers)
        FollowRecord.Type.FOLLOWING -> stringResource(id = R.string.feature_feed_following)
    }

    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                title = title,
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(FollowRecordUiEvent.OnBackClick) }
                )
            )
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Preview
@Composable
private fun FollowRecordItemContentPreview() {
    FollowRecordItem(user = UiUser.DEFAULT, onClick = { })
}

@Preview
@Composable
private fun FollowRecordContentPreview() {
    FollowRecordContent(state = FollowRecordUiState(), onEvent = { })
}