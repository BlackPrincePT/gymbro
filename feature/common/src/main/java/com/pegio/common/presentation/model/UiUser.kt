package com.pegio.common.presentation.model

import com.pegio.model.User.Gender

data class UiUser(
    val id: String,
    val username: String,
    val age: String,
    val gender: Gender,
    val heightCm: String,
    val weightKg: String,
    val avatarUrl: String?,
    val imgBackgroundUrl: String?,
    val followingCount: Int,
    val followersCount: Int
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
            followingCount = 101,
            followersCount = 102
        )

        val EMPTY = UiUser(
            id = "",
            username = "",
            age = "",
            gender = Gender.MALE,
            heightCm = "",
            weightKg = "",
            avatarUrl = null,
            imgBackgroundUrl = null,
            followingCount = 0,
            followersCount = 0
        )
    }
}
