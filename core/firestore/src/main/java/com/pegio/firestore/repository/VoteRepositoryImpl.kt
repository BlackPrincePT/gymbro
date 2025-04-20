package com.pegio.firestore.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.domain.repository.VoteRepository
import com.pegio.firestore.core.FirestoreConstants.POSTS
import com.pegio.firestore.core.FirestoreConstants.VOTES
import com.pegio.firestore.model.VoteDto
import com.pegio.firestore.model.mapper.VoteDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.Vote
import javax.inject.Inject

internal class VoteRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val voteDtoMapper: VoteDtoMapper
) : VoteRepository {

    override fun votePost(postId: String, vote: Vote) {
        val voteDto = voteDtoMapper.mapFromDomain(vote)
        db.collection(POSTS).document(postId).collection(VOTES).document(vote.voterId).set(voteDto)
    }

    override suspend fun checkForCommentVote(voterId: String, commentId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun checkForPostVote(voterId: String, postId: String): Resource<Vote, DataError.Firestore> {
        val documentRef = db.collection(POSTS).document(postId).collection(VOTES).document(voterId)

        val result = firestoreUtils.readDocument(documentRef, VoteDto::class.java)

        return result.map(voteDtoMapper::mapToDomain)
    }
}