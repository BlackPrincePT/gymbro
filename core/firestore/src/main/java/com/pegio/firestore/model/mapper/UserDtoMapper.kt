package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.UserDto
import com.pegio.model.User
import javax.inject.Inject

internal class UserDtoMapper @Inject constructor() : Mapper<UserDto, User> {

    override fun mapToDomain(data: UserDto): User {
        return User(
            id = data.id ?: throw IllegalStateException(),
            username = data.username,
            age = data.age,
            gender = data.gender,
            heightCm = data.heightCm,
            weightKg = data.weightKg,
            imgProfileUrl = data.imgProfileUrl,
            imgBackgroundUrl = data.imgBackgroundUrl
        )
    }

    override fun mapFromDomain(data: User): UserDto {
        return UserDto(
            id = data.id.ifEmpty { null },
            username = data.username,
            age = data.age,
            gender = data.gender,
            heightCm = data.heightCm,
            weightKg = data.weightKg,
            imgProfileUrl = data.imgProfileUrl,
            imgBackgroundUrl = data.imgBackgroundUrl
        )
    }
}