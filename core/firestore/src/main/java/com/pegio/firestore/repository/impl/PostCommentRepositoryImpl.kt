package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.mapList
import com.pegio.firestore.repository.PostCommentRepository
import com.pegio.firestore.core.FirestoreConstants.COMMENTS
import com.pegio.firestore.core.FirestoreConstants.POSTS
import com.pegio.firestore.core.FirestoreConstants.TIMESTAMP
import com.pegio.firestore.core.FirestorePagingSource
import com.pegio.firestore.model.PostCommentDto
import com.pegio.firestore.model.mapper.PostCommentDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.PostComment
import javax.inject.Inject

internal class PostCommentRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val postCommentDtoMapper: PostCommentDtoMapper,
    firestoreUtils: FirestoreUtils,
) : PostCommentRepository {

    private companion object {
        const val COMMENT_PAGE_SIZE: Long = 10L
    }

    private val commentsPagingSource =
        FirestorePagingSource(COMMENT_PAGE_SIZE, PostCommentDto::class.java, firestoreUtils)

    override fun writeComment(comment: PostComment, postId: String) {
        val commentDto = postCommentDtoMapper.mapFromDomain(comment)
        db.collection(POSTS).document(postId).collection(COMMENTS).add(commentDto)
    }

    override suspend fun fetchNextCommentsPage(postId: String): Resource<List<PostComment>, DataError> {
        val baseQuery = db.collection(POSTS)
            .document(postId)
            .collection(COMMENTS)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)

        return commentsPagingSource.loadNextPage(baseQuery)
            .mapList(postCommentDtoMapper::mapToDomain)
    }
}