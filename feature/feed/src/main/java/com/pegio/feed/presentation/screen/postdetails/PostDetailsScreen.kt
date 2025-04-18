package com.pegio.feed.presentation.screen.postdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.feed.R
import com.pegio.feed.presentation.component.PostCommentContent
import com.pegio.feed.presentation.component.PostContent
import com.pegio.feed.presentation.component.WriteCommentContent
import com.pegio.feed.presentation.model.UiPostComment
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEffect
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEvent
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiState

@Composable
internal fun PostDetailsScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: PostDetailsViewModel = hiltViewModel()
) {
    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Navigation
            PostDetailsUiEffect.NavigateBack -> onBackClick()
        }
    }

    PostDetailsContent(viewModel.uiState, viewModel::onEvent)
}

@Composable
private fun PostDetailsContent(
    state: PostDetailsUiState,
    onEvent: (PostDetailsUiEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            PostContent(
                post = state.displayedPost,
                onUpVoteClick = { },
                onDownVoteClick = { },
                onCommentClick = { },
                onRatingClick = { }
            )
        }

        item {
            WriteCommentContent(
                value = state.commentText,
                onValueChange = { onEvent(PostDetailsUiEvent.OnCommentTextChange(it)) },
                label = stringResource(R.string.feature_feed_write_a_comment),
                onSubmit = { onEvent(PostDetailsUiEvent.OnCommentSubmit) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        items(state.comments) { comment ->
            PostCommentContent(
                avatarUrl = comment.author.avatarUrl,
                username = comment.author.username,
                commentText = comment.content,
                commentDate = comment.date
            )
        }
    }
}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (PostDetailsUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(PostDetailsUiEvent.OnBackClick) }
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostDetailsContentPreview() {
    PostDetailsContent(
        state = PostDetailsUiState(comments = List(size = 3) { UiPostComment.DEFAULT }),
        onEvent = { }
    )
}