package com.pegio.gymbro.domain.model

data class Post(
    val id: String,
    val authorId: String,
    val content: String,
    val imageUrl: String?,
    val upVotesInLast24Hours: Int,
    val voteCount: Int,
    val commentCount: Int,
    val ratingAverage: Double,
    val ratingCount: Int,
    val timestamp: Long
)