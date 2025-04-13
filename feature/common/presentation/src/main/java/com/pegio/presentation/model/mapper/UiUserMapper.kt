package com.pegio.presentation.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.domain.model.User
import com.pegio.presentation.model.UiUser
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
            imgProfileUrl = data.imgProfileUrl,
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
            imgProfileUrl = data.imgProfileUrl,
            imgBackgroundUrl = data.imgBackgroundUrl,
        )
    }
}