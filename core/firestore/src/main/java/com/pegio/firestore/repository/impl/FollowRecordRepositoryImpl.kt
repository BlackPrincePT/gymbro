package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.common.core.mapList
import com.pegio.firestore.core.FirestoreConstants.FOLLOWERS
import com.pegio.firestore.core.FirestoreConstants.FOLLOWING
import com.pegio.firestore.core.FirestoreConstants.TIMESTAMP
import com.pegio.firestore.core.FirestoreConstants.USERS
import com.pegio.firestore.core.FirestorePagingSource
import com.pegio.firestore.model.FollowRecordDto
import com.pegio.firestore.model.mapper.FollowRecordDtoMapper
import com.pegio.firestore.repository.FollowRecordRepository
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.FollowRecord
import javax.inject.Inject

internal class FollowRecordRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val followRecordDtoMapper: FollowRecordDtoMapper
) : FollowRecordRepository {


    private companion object {
        const val FOLLOW_RECORD_PAGE_SIZE: Long = 20L
    }

    private val followRecordsPagingSource =
        FirestorePagingSource(FOLLOW_RECORD_PAGE_SIZE, FollowRecordDto::class.java, firestoreUtils)


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun followUser(userId: String, targetUserId: String) {
        val followRecordDto = FollowRecordDto(timestamp = System.currentTimeMillis())
        db.collection(USERS).apply {
            document(userId).collection(FOLLOWING).document(targetUserId).set(followRecordDto)
            document(targetUserId).collection(FOLLOWERS).document(userId).set(followRecordDto)
        }
    }

    override fun unfollowUser(userId: String, targetUserId: String) {
        db.collection(USERS).apply {
            document(userId).collection(FOLLOWING).document(targetUserId).delete()
            document(targetUserId).collection(FOLLOWERS).document(userId).delete()
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override suspend fun fetchFollowing(userId: String): Resource<List<FollowRecord>, DataError> {
        return followRecordsPagingSource.loadNextPage(getBaseQuery(userId, FollowRecord.Type.FOLLOWING))
            .mapList(followRecordDtoMapper::mapToDomain)
    }

    override suspend fun fetchFollowers(userId: String): Resource<List<FollowRecord>, DataError> {
        return followRecordsPagingSource.loadNextPage(getBaseQuery(userId, FollowRecord.Type.FOLLOWERS))
            .mapList(followRecordDtoMapper::mapToDomain)
    }

    private fun getBaseQuery(userId: String, type: FollowRecord.Type): Query {
        val subCollectionName = when (type) {
            FollowRecord.Type.FOLLOWERS -> FOLLOWERS
            FollowRecord.Type.FOLLOWING -> FOLLOWING
        }

        return db.collection(USERS)
            .document(userId)
            .collection(subCollectionName)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override suspend fun isUserFollowing(
        userId: String,
        targetUserId: String
    ): Resource<FollowRecord, DataError.Firestore> {
        val documentRef =
            db.collection(USERS).document(userId).collection(FOLLOWING).document(targetUserId)

        val result = firestoreUtils.readDocument(documentRef, FollowRecordDto::class.java)

        return result.map(followRecordDtoMapper::mapToDomain)
    }
}