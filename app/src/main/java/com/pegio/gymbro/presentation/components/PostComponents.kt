package com.pegio.gymbro.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pegio.gymbro.R
import com.pegio.gymbro.presentation.model.UiPost

@Composable
private fun Post(
    post: UiPost
) {
    Card(shape = MaterialTheme.shapes.large) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            PostHeader(post, onProfileClick = { })

            Spacer(modifier = Modifier.height(8.dp))

            PostContent(post, onImageClick = { })

            Spacer(modifier = Modifier.height(8.dp))

            PostActions(
                post = post,
                onUpVoteClick = { },
                onDownVoteClick = { },
                onCommentClick = { },
                onRatingClick = { }
            )
        }
    }
}

// ====== ====== Post Content ====== ====== \\

@Composable
private fun PostContent(
    post: UiPost,
    onImageClick: () -> Unit = { }
) {
    Column {
        Text(
            text = post.content,
            maxLines = 3,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        AsyncImage(
            model = post.imageUrl,
            contentDescription = stringResource(R.string.post_image),
            fallback = ColorPainter(Color.Gray),
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .clickable { onImageClick.invoke() }
                .height(320.dp)
        )
    }
}

// ====== ====== Post Info ====== ====== \\

@Composable
private fun PostHeader(
    post: UiPost,
    onProfileClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        ProfileImage(
            imageUrl = post.author.imgProfileUrl,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onProfileClick.invoke() }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = post.author.username,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { onProfileClick.invoke() }
            )

            Text(
                text = post.publishedDate,
                color = Color.Gray
            )
        }
    }
}

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
            commentCount = post.commentsCount
        )

        Spacer(modifier = Modifier.weight(1f))

        RatingAction(
            rating = post.averageRating,
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
            text = voteCount,
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
private fun PostHeaderPreview() {
    PostHeader(
        post = UiPost.DEFAULT,
        onProfileClick = { }
    )
}

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

@Preview
@Composable
private fun PostPreview() {
    Post(post = UiPost.DEFAULT)
}