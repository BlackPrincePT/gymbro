package com.pegio.gymbro.domain.model

data class User(
    val id: String,
    val username: String,
    val imgProfileUrl: String?,
    val imgBackgroundUrl: String?
)