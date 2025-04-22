package com.pegio.common.presentation.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.common.presentation.model.UiUser
import com.pegio.model.User
import javax.inject.Inject

class UiUserMapper @Inject constructor() : Mapper<UiUser, User> {

    override fun mapToDomain(data: UiUser): User = with(data) {
        return User(
            id = id,
            username = username,
            age = age.toInt(),
            gender = gender ?: User.Gender.MALE, // FIXME PLEASE
            heightCm = heightCm.toInt(),
            weightKg = weightKg.toInt(),
            imgProfileUrl = avatarUrl,
            imgBackgroundUrl = imgBackgroundUrl,
            followingCount = followingCount,
            followersCount = followersCount
        )
    }

    override fun mapFromDomain(data: User): UiUser = with(data) {
        return UiUser(
            id = id,
            username = username,
            age = age.toString(),
            gender = gender,
            heightCm = heightCm.toString(),
            weightKg = weightKg.toString(),
            avatarUrl = imgProfileUrl,
            imgBackgroundUrl = imgBackgroundUrl,
            followingCount = followingCount,
            followersCount = followersCount
        )
    }
}