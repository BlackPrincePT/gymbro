package com.pegio.gymbro.presentation.mapper

import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.User
import com.pegio.gymbro.presentation.model.UiUser
import javax.inject.Inject

class UiUserMapper @Inject constructor() : Mapper<UiUser, User> {

    override fun mapToDomain(data: UiUser): User {
        return User(
            id = data.id,
            username = data.username,
            imgProfileUrl = data.imgProfileUrl,
            imgBackgroundUrl = data.imgBackgroundUrl
        )
    }

    override fun mapFromDomain(data: User): UiUser {
        return UiUser(
            id = data.id,
            username = data.username,
            imgProfileUrl = data.imgProfileUrl,
            imgBackgroundUrl = data.imgBackgroundUrl
        )
    }
}