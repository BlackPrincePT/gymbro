package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.firestore.repository.FollowRecordRepository
import com.pegio.firestore.core.FirestoreConstants.FOLLOWERS
import com.pegio.firestore.core.FirestoreConstants.FOLLOWING
import com.pegio.firestore.core.FirestoreConstants.USERS
import com.pegio.firestore.model.FollowRecordDto
import com.pegio.firestore.model.mapper.FollowRecordDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.FollowRecord
import javax.inject.Inject

internal class FollowRecordRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val followRecordDtoMapper: FollowRecordDtoMapper
) : FollowRecordRepository {

    override fun followUser(userId: String, targetUserId: String) {
        val follower = FollowRecordDto(timestamp = System.currentTimeMillis())
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

    override suspend fun isUserFollowing(userId: String, targetUserId: String): Resource<FollowRecord, DataError.Firestore> {
        val documentRef =
            db.collection(USERS).document(userId).collection(FOLLOWING).document(targetUserId)

        val result = firestoreUtils.readDocument(documentRef, FollowRecordDto::class.java)

        return result.map(followRecordDtoMapper::mapToDomain)
    }
}