package com.pegio.firestore.model.mapper

import com.pegio.common.core.ToDomainMapper
import com.pegio.firestore.model.FollowerDto
import com.pegio.model.Follower
import javax.inject.Inject

class FollowerDtoMapper @Inject constructor() : ToDomainMapper<FollowerDto, Follower> {

    override fun mapToDomain(data: FollowerDto): Follower = with(data) {
        return Follower(
            followerId = id ?: throw IllegalStateException(),
            timestamp = timestamp
        )
    }
}