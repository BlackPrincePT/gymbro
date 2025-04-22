package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.UserDto
import com.pegio.model.User
import javax.inject.Inject

internal class UserDtoMapper @Inject constructor() : Mapper<UserDto, User> {

    override fun mapToDomain(data: UserDto): User = with(data) {
        return User(
            id = id ?: throw IllegalStateException(),
            username = username,
            age = age,
            gender = gender,
            heightCm = heightCm,
            weightKg = weightKg,
            imgProfileUrl = imgProfileUrl,
            imgBackgroundUrl = imgBackgroundUrl,
            followingCount = followingCount,
            followersCount = followersCount
        )
    }

    override fun mapFromDomain(data: User): UserDto = with(data) {
        return UserDto(
            id = id.ifEmpty { null },
            username = username,
            age = age,
            gender = gender,
            heightCm = heightCm,
            weightKg = weightKg,
            imgProfileUrl = imgProfileUrl,
            imgBackgroundUrl = imgBackgroundUrl,
            followingCount = followingCount,
            followersCount = followersCount
        )
    }
}