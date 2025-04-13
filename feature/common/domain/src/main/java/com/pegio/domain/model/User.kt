package com.pegio.domain.model

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
    enum class Gender {
        MALE,
        FEMALE
    }
}