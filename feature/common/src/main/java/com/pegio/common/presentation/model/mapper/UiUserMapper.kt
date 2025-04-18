package com.pegio.common.presentation.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.common.presentation.model.UiUser
import com.pegio.model.User
import javax.inject.Inject

class UiUserMapper @Inject constructor() : Mapper<UiUser, User> {

    override fun mapToDomain(data: UiUser): User {
        return User(
            id = data.id,
            username = data.username,
            age = data.age.toInt(),
            gender = data.gender ?: User.Gender.MALE, // FIXME PLEASE
            heightCm = data.heightCm.toInt(),
            weightKg = data.weightKg.toInt(),
            imgProfileUrl = data.avatarUrl,
            imgBackgroundUrl = data.imgBackgroundUrl,
        )
    }

    override fun mapFromDomain(data: User): UiUser {
        return UiUser(
            id = data.id,
            username = data.username,
            age = data.age.toString(),
            gender = data.gender,
            heightCm = data.heightCm.toString(),
            weightKg = data.weightKg.toString(),
            avatarUrl = data.imgProfileUrl,
            imgBackgroundUrl = data.imgBackgroundUrl,
        )
    }
}