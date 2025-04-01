package com.pegio.gymbro.presentation.model

data class UiPost(
    val id: String,
    val author: UiUser,
    val content: String,
    val imageUrl: String?,
    val upVoteCount: Int,
    val downVoteCount: Int,
    val commentsCount: String,
    val averageRating: String,
    val publishedDate: String,
) {

    val totalVotes: String
        get() = (upVoteCount + downVoteCount).toString()

    val hasImage: Boolean
        get() = imageUrl != null

    companion object {

        val DEFAULT = UiPost(
            id = "",
            author = UiUser.DEFAULT,
            content = "Hello, TBC IT Academy!",
            imageUrl = null,
            upVoteCount = 108,
            downVoteCount = 53,
            commentsCount = "13",
            averageRating = "3.5",
            publishedDate = "01/04/25"
        )
    }
}