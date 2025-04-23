package com.pegio.firestore.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.FollowRecord

interface FollowRecordRepository {
    fun followUser(userId: String, targetUserId: String)
    fun unfollowUser(userId: String, targetUserId: String)
    suspend fun fetchFollowing(userId: String) : Resource<List<FollowRecord>, DataError>
    suspend fun fetchFollowers(userId: String) : Resource<List<FollowRecord>, DataError>
    suspend fun isUserFollowing(userId: String, targetUserId: String) : Resource<FollowRecord, DataError.Firestore>
}
