package com.pegio.presentation.model

data class UiPost(
    val id: String,
    val author: UiUser,
    val content: String,
    val imageUrl: String?,
    val voteCount: String,
    val commentCount: String,
    val ratingAverage: String,
    val ratingCount: String,
    val publishedDate: String
) {

    val hasImage: Boolean
        get() = imageUrl != null

    companion object {

        val DEFAULT = UiPost(
            id = "",
            author = UiUser.DEFAULT,
            content = "Hello, TBC IT Academy!",
            imageUrl = null,
            voteCount = "108",
            commentCount = "13",
            ratingAverage = "3.5",
            ratingCount = "23",
            publishedDate = "01/04/25"
        )
    }
}