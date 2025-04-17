package com.pegio.model

data class User(
    val id: String,
    val username: String,
    val age: Int,
    val gender: Gender,
    val heightCm: Int,
    val weightKg: Int,
    val imgProfileUrl: String?,
    val imgBackgroundUrl: String?
) {

    data class Auth(
        val id: String,
        val isAnonymous: Boolean
    )

    enum class Gender {
        MALE,
        FEMALE
    }
}