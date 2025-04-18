package com.pegio.feed.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pegio.common.presentation.model.UiUser
import com.pegio.designsystem.component.PostImage
import com.pegio.designsystem.component.ProfileImage
import com.pegio.feed.presentation.model.UiPost

@Composable
internal fun PostContent(
    post: UiPost,
    onUpVoteClick: () -> Unit,
    onDownVoteClick: () -> Unit,
    onCommentClick: () -> Unit,
    onRatingClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        PostHeader(post, onProfileClick = { })

        Spacer(modifier = Modifier.height(8.dp))

        PostContent(post)

        Spacer(modifier = Modifier.height(8.dp))

        PostActions(
            post = post,
            onUpVoteClick = onUpVoteClick,
            onDownVoteClick = onDownVoteClick,
            onCommentClick = onCommentClick,
            onRatingClick = onRatingClick
        )
    }
}

// ====== ====== Post Content ====== ====== \\

@Composable
private fun PostContent(
    post: UiPost
) {
    Column {
        Text(
            text = post.content,
            maxLines = 3,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        if (post.hasImage) {
            Spacer(modifier = Modifier.height(4.dp))

            PostImage(
                imageUrl = post.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
        }
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
            imageUrl = post.author.avatarUrl,
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
            voteCount = post.voteCount,
            onUpVoteClick = onUpVoteClick,
            onDownVoteClick = onDownVoteClick
        )

        CommentAction(
            onClick = onCommentClick,
            commentCount = post.commentCount
        )

        Spacer(modifier = Modifier.weight(1f))

        RatingAction(
            rating = post.ratingAverage,
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
    ) {
        PostAction(
            onClick = onClick,
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
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

// ====== ====== Create ====== ====== \\

@Composable
internal fun CreatePost(
    currentUser: UiUser,
    onPostClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPostClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
        ) {
            ProfileImage(
                imageUrl = currentUser.avatarUrl,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .clickable { onProfileClick.invoke() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "What's on your muscles?",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
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
    PostContent(
        post = UiPost.DEFAULT,
        onUpVoteClick = { },
        onDownVoteClick = { },
        onCommentClick = { },
        onRatingClick = { }
    )
}

@Preview
@Composable
private fun CreatePostPreview() {
    CreatePost(
        currentUser = UiUser.DEFAULT,
        onPostClick = { },
        onProfileClick = { }
    )
}