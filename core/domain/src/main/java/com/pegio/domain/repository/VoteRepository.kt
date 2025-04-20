package com.pegio.domain.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.Vote

interface VoteRepository {

    // Upload
    fun votePost(postId: String, vote: Vote)

    // Fetch
    suspend fun checkForPostVote(voterId: String, postId: String): Resource<Vote, DataError.Firestore>
    suspend fun checkForCommentVote(voterId: String, commentId: String)
}