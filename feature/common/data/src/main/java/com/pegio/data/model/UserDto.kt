package com.pegio.data.model

import com.google.firebase.firestore.DocumentId
import com.pegio.domain.model.User.Gender

data class UserDto(
    @DocumentId val id: String? = null,
    val username: String = "",
    val age: Int = 0,
    val gender: Gender = Gender.MALE,
    val heightCm: Int = 0,
    val weightKg: Int = 0,
    val imgProfileUrl: String? = null,
    val imgBackgroundUrl: String? = null,
)