package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.VoteDto
import com.pegio.model.Vote
import javax.inject.Inject

internal class VoteDtoMapper @Inject constructor() : Mapper<VoteDto, Vote> {

    override fun mapToDomain(data: VoteDto): Vote {
        return Vote(
            voterId = data.id ?: throw IllegalStateException(),
            vote = Vote.Type.valueOf(value = data.vote),
            timestamp = data.timestamp
        )
    }

    override fun mapFromDomain(data: Vote): VoteDto {
        return VoteDto(
            id = data.voterId,
            vote = data.vote.value,
            timestamp = data.timestamp
        )
    }
}