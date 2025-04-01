package com.pegio.gymbro.presentation.model

data class UiPost(
    val id: String,
    val postOwnerId: String,
    val text: String,
    val imageUrl: String?,
    val upVotes: Int,
    val downVotes: Int,
    val commentCount: String,
    val rating: String,
    val date: String,
) {

    val totalVotes: String
        get() = (upVotes + downVotes).toString()

    val hasImage: Boolean
        get() = imageUrl != null

    companion object {

        val DEFAULT = UiPost(
            id = "",
            postOwnerId = "",
            text = "Hello, TBC IT Academy!",
            imageUrl = null,
            upVotes = 108,
            downVotes = 53,
            commentCount = "13",
            rating = "3.5",
            date = "01/04/25"
        )
    }
}