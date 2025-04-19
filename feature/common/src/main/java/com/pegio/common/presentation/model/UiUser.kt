package com.pegio.common.presentation.model

import com.pegio.model.User.Gender

data class UiUser(
    val id: String,
    val username: String,
    val age: String,
    val gender: Gender?,
    val heightCm: String,
    val weightKg: String,
    val avatarUrl: String?,
    val imgBackgroundUrl: String?,
) {

    companion object {

        val DEFAULT = UiUser(
            id = "",
            username = "Pitiful Android Developer",
            age = "21",
            gender = Gender.MALE,
            heightCm = "180",
            weightKg = "75",
            avatarUrl = null,
            imgBackgroundUrl = null,
        )

        val EMPTY = UiUser(
            id = "",
            username = "",
            age = "",
            gender = null,
            heightCm = "",
            weightKg = "",
            avatarUrl = null,
            imgBackgroundUrl = null
        )
    }
}
