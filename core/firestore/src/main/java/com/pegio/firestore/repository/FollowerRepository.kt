package com.pegio.firestore.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.Follower

interface FollowerRepository {
    fun followUser(userId: String, targetUserId: String)
    fun unfollowUser(userId: String, targetUserId: String)
    suspend fun isUserFollowing(userId: String, targetUserId: String) : Resource<Follower, DataError.Firestore>
}
