package com.pegio.model

data class Story(
    val id: String,
    val authorId: String,
    val media: Media,
    val createdAt: Long,
    val expiresAt: Long
) {

    val isExpired: Boolean
        get() = System.currentTimeMillis() > expiresAt

    constructor(id: String, authorId: String, media: Media, createdAt: Long) : this(
        id = id,
        authorId = authorId,
        media = media,
        createdAt = createdAt,
        expiresAt = createdAt + (24 * 60 * 60 * 1000)
    )
}