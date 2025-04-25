package com.pegio.feed.presentation.screen.postdetails

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.components.EmptyLoadingScreen
import com.pegio.common.presentation.components.LoadingItemsIndicator
import com.pegio.common.presentation.components.ProfileImage
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.component.GymBroIconButton
import com.pegio.designsystem.component.GymBroTextButton
import com.pegio.designsystem.component.GymBroTextField
import com.pegio.feed.R
import com.pegio.feed.presentation.component.PostContent
import com.pegio.feed.presentation.model.UiPostComment
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEffect
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEvent
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiState

@Composable
internal fun PostDetailsScreen(
    onBackClick: () -> Unit,
    onUserProfileClick: (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: PostDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Failure
            is PostDetailsUiEffect.ShowSnackbar -> onShowSnackbar(context.getString(effect.errorRes))

            // Navigation
            PostDetailsUiEffect.NavigateBack -> onBackClick()
            is PostDetailsUiEffect.NavigateToUserProfile -> onUserProfileClick(effect.userId)
        }
    }

    with(viewModel.uiState) {
        when {
            isLoading -> EmptyLoadingScreen()
            else -> PostDetailsContent(state = this, onEvent = viewModel::onEvent)
        }
    }
}

@Composable
private fun PostDetailsContent(
    state: PostDetailsUiState,
    onEvent: (PostDetailsUiEvent) -> Unit
) = with(state) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        item {
            PostContent(
                post = displayedPost,
                onProfileClick = { onEvent(PostDetailsUiEvent.OnUserProfileClick(userId = displayedPost.author.id)) },
                onVoteClick = { onEvent(PostDetailsUiEvent.OnPostVote(voteType = it)) }
            )
        }

        item {
            GymBroTextField(
                value = commentText,
                onValueChange = { onEvent(PostDetailsUiEvent.OnCommentTextChange(it)) },
                label = stringResource(R.string.feature_feed_write_a_comment),
                trailingIcon = {
                    UploadCommentContent(
                        isUploading = loadingSendComment,
                        onClick = { onEvent(PostDetailsUiEvent.OnCommentSubmitClick) }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

        if (comments.isEmpty() && !loadingMoreComments) item {
            EmptyCommentsContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
        else items(comments) { comment ->
            PostCommentContent(
                avatarUrl = comment.author.avatarUrl,
                username = comment.author.username,
                commentText = comment.content,
                commentDate = comment.date,
                onProfileClick = { onEvent(PostDetailsUiEvent.OnUserProfileClick(comment.author.id)) }
            )
        }

        if (!endOfCommentsReached) item {
            if (loadingMoreComments) LoadingItemsIndicator()
            else if (comments.isNotEmpty())
                LoadMoreItemsTextButton(onClick = { onEvent(PostDetailsUiEvent.OnLoadMoreCommentsClick) })
        }
    }
}

@Composable
private fun EmptyCommentsContent(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.feature_feed_be_first_one_to_comment),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        color = Color.Gray,
        modifier = modifier
    )
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun PostCommentContent(
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
private fun UploadCommentContent(
    isUploading: Boolean,
    onClick: () -> Unit
) {
    if (isUploading) CircularProgressIndicator(modifier = Modifier.size(32.dp))
    else GymBroIconButton(
        imageVector = Icons.AutoMirrored.Default.Send,
        onClick = onClick,
        modifier = Modifier.size(32.dp)
    )
}

@Composable
private fun LoadMoreItemsTextButton(
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        GymBroTextButton(
            text = stringResource(R.string.feature_feed_load_more_comments),
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 16.dp)
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


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


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


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

@Preview(showBackground = true)
@Composable
private fun PostDetailsContentPreview() {
    PostDetailsContent(
        state = PostDetailsUiState(comments = List(size = 3) { UiPostComment.DEFAULT }),
        onEvent = { }
    )
}