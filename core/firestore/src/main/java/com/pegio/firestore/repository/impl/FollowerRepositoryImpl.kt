package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.firestore.repository.FollowerRepository
import com.pegio.firestore.core.FirestoreConstants.FOLLOWERS
import com.pegio.firestore.core.FirestoreConstants.FOLLOWING
import com.pegio.firestore.core.FirestoreConstants.USERS
import com.pegio.firestore.model.FollowerDto
import com.pegio.firestore.model.mapper.FollowerDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.Follower
import javax.inject.Inject

internal class FollowerRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val followerDtoMapper: FollowerDtoMapper
) : FollowerRepository {

    override fun followUser(userId: String, targetUserId: String) {
        val follower = FollowerDto(timestamp = System.currentTimeMillis())
        db.collection(USERS).apply {
            document(userId).collection(FOLLOWING).document(targetUserId).set(follower)
            document(targetUserId).collection(FOLLOWERS).document(userId).set(follower)
        }
    }

    override fun unfollowUser(userId: String, targetUserId: String) {
        db.collection(USERS).apply {
            document(userId).collection(FOLLOWING).document(targetUserId).delete()
            document(targetUserId).collection(FOLLOWERS).document(userId).delete()
        }
    }

    override suspend fun isUserFollowing(userId: String, targetUserId: String): Resource<Follower, DataError.Firestore> {
        val documentRef =
            db.collection(USERS).document(userId).collection(FOLLOWING).document(targetUserId)

        val result = firestoreUtils.readDocument(documentRef, FollowerDto::class.java)

        return result.map(followerDtoMapper::mapToDomain)
    }
}