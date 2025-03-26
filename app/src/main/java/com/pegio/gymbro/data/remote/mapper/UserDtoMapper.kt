package com.pegio.gymbro.data.remote.mapper

import com.pegio.gymbro.data.remote.model.UserDto
import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.User
import javax.inject.Inject

class UserDtoMapper @Inject constructor() : Mapper<UserDto, User> {

    override fun mapToDomain(data: UserDto): User {
        return User(
            id = data.id ?: throw Exception("User ID cannot be null"),
            username = data.username,
            profile = data.profile
        )
    }

    override fun mapFromDomain(data: User): UserDto {
        return UserDto(
            id = data.id.ifEmpty { null },
            username = data.username,
            profile = data.profile
        )
    }
}