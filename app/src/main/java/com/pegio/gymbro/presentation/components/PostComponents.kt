package com.pegio.gymbro.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pegio.gymbro.R
import com.pegio.gymbro.presentation.model.UiPost

// ====== ====== Post Actions ====== ====== \\

@Composable
private fun PostActions(
    post: UiPost,
    onUpVoteClick: () -> Unit,
    onDownVoteClick: () -> Unit,
    onCommentClick: () -> Unit,
    onRatingClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        VoteActions(
            voteCount = post.totalVotes,
            onUpVoteClick = onUpVoteClick,
            onDownVoteClick = onDownVoteClick
        )

        CommentAction(
            onClick = onCommentClick,
            commentCount = post.commentCount
        )

        Spacer(modifier = Modifier.weight(1f))

        RatingAction(
            rating = post.rating,
            onClick = onRatingClick
        )
    }
}

@Composable
private fun VoteActions(
    voteCount: String,
    onUpVoteClick: () -> Unit,
    onDownVoteClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        PostAction(onUpVoteClick, Icons.Default.ArrowUpward)

        Text(
            text = voteCount.toString(),
            color = Color.Gray,
            fontSize = 16.sp
        )

        PostAction(onDownVoteClick, Icons.Default.ArrowDownward)
    }
}

@Composable
private fun CommentAction(
    commentCount: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick.invoke() }
    ) {
        PostAction(
            onClick = { },
            imageVector = Icons.AutoMirrored.Default.Comment
        )

        Text(
            text = commentCount,
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun RatingAction(
    rating: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick.invoke() }
    ) {
        PostAction(
            onClick = { },
            imageVector = Icons.Default.Star
        )

        Text(
            text = rating,
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun PostAction(
    onClick: () -> Unit,
    imageVector: ImageVector
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(R.string.vote_button),
            tint = Color.Gray,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

// ====== ====== Preview ====== ====== \\

@Preview
@Composable
private fun PostActionsPreview() {
    PostActions(
        post = UiPost.DEFAULT,
        onUpVoteClick = { },
        onDownVoteClick = { },
        onCommentClick = { },
        onRatingClick = { }
    )
}