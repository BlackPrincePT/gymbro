package com.pegio.gymbro.presentation.model

data class UiUser(
    val id: String,
    val username: String,
    val imgProfileUrl: String?,
    val imgBackgroundUrl: String?
) {

    companion object {
        val EMPTY = UiUser(
            id = "",
            username = "Pitiful Android Developer",
            imgProfileUrl = null,
            imgBackgroundUrl = null
        )
    }
}