package com.pegio.model

data class Post(
    val id: String,
    val authorId: String,
    val content: String,
    val imageUrl: String?,
    val workoutId: String?,
    val upVotesInLast24Hours: Int,
    val voteCount: Int,
    val commentCount: Int,
    val ratingAverage: Double,
    val ratingCount: Int,
    val timestamp: Long
) {

    companion object {

        val EMPTY = Post(
            id = "",
            authorId = "",
            content = "",
            imageUrl = null,
            workoutId = null,
            upVotesInLast24Hours = 0,
            voteCount = 0,
            commentCount = 0,
            ratingAverage = 0.0,
            ratingCount = 0,
            timestamp = 0
        )
    }
}