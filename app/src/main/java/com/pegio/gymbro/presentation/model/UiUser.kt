package com.pegio.gymbro.presentation.model

import com.pegio.gymbro.domain.model.User.Gender

data class UiUser(
    val id: String,
    val username: String,
    val age: String,
    val gender: Gender?,
    val heightCm: String,
    val weightKg: String,
    val imgProfileUrl: String?,
    val imgBackgroundUrl: String?,
) {

    companion object {
        val test = UiUser(
            id = "",
            username = "TBCBANK",
            age =  "94",
            heightCm = "123",
            weightKg = "223",
            imgProfileUrl = null,
            imgBackgroundUrl = null,
            gender = Gender.MALE,
        )
        val DEFAULT = UiUser(
            id = "",
            username = "Pitiful Android Developer",
            age = "21",
            gender = Gender.MALE,
            heightCm = "180",
            weightKg = "75",
            imgProfileUrl = null,
            imgBackgroundUrl = null,
        )

        val EMPTY = UiUser(
            id = "",
            username = "",
            age = "",
            gender = null,
            heightCm = "",
            weightKg = "",
            imgProfileUrl = null,
            imgBackgroundUrl = null
        )
    }
}
