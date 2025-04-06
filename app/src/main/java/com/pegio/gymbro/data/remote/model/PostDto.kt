package com.pegio.gymbro.data.remote.model

import com.google.firebase.firestore.DocumentId

data class PostDto(
    @DocumentId val id: String? = null,
    val authorId: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val upVotesInLast24Hours: Int = 0,
    val voteCount: Int = 0,
    val commentCount: Int = 0,
    val ratingAverage: Double = 0.0,
    val ratingCount: Int = 0,
    val timestamp: Long = 0L
)