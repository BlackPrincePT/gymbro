package com.pegio.gymbro.presentation.mapper

import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.User
import com.pegio.gymbro.presentation.model.UiUser
import javax.inject.Inject

class UiUserMapper @Inject constructor(
    private val aiChatMessageMapper: AiChatMessageMapper
) : Mapper<UiUser, User> {

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