package com.pegio.domain.model

import com.pegio.model.Post
import com.pegio.model.User
import com.pegio.model.Vote

data class PostWithAuthorAndVote(
    val post: Post,
    val author: User?,
    val currentUserVote: Vote?
)