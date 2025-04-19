package com.pegio.model

data class PostComment(
    val id: String,
    val authorId: String,
    val content: String,
    val timestamp: Long
)