package com.pegio.gymbro.data.remote.mapper

import com.pegio.gymbro.data.remote.model.UserDto
import com.pegio.gymbro.domain.core.InvalidDocumentIdException
import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.User
import javax.inject.Inject

class UserDtoMapper @Inject constructor() : Mapper<UserDto, User> {

    override fun mapToDomain(data: UserDto): User {
        return User(
            id = data.id ?: throw InvalidDocumentIdException(),
            username = data.username,
            imgProfileUrl = data.imgProfileUrl,
            imgBackgroundUrl = data.imgBackgroundUrl
        )
    }

    override fun mapFromDomain(data: User): UserDto {
        return UserDto(
            id = data.id.ifEmpty { null },
            username = data.username,
            imgProfileUrl = data.imgProfileUrl,
            imgBackgroundUrl = data.imgBackgroundUrl
        )
    }
}