package com.pegio.firestore.model.mapper

import com.pegio.common.core.ToDomainMapper
import com.pegio.firestore.model.FollowRecordDto
import com.pegio.model.FollowRecord
import javax.inject.Inject

internal class FollowRecordDtoMapper @Inject constructor() : ToDomainMapper<FollowRecordDto, FollowRecord> {

    override fun mapToDomain(data: FollowRecordDto): FollowRecord = with(data) {
        return FollowRecord(
            userId = id ?: throw IllegalStateException()
        )
    }
}