package com.pegio.firestore.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.domain.repository.PostCommentRepository
import com.pegio.firestore.core.FirestoreConstants.COMMENTS
import com.pegio.firestore.core.FirestoreConstants.POSTS
import com.pegio.firestore.core.FirestoreConstants.TIMESTAMP
import com.pegio.firestore.core.FirestoreConstants.UP_VOTES_IN_LAST_24_HOURS
import com.pegio.firestore.core.FirestoreConstants.USERS
import com.pegio.firestore.model.PostCommentDto
import com.pegio.firestore.model.PostDto
import com.pegio.firestore.model.mapper.PostCommentDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.PostComment
import org.w3c.dom.Comment
import javax.inject.Inject

class PostCommentRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val postCommentDtoMapper: PostCommentDtoMapper
) : PostCommentRepository {

    companion object {
        private const val COMMENT_PAGE_SIZE: Long = 10L
    }

    override fun writeComment(comment: PostComment, postId: String) {
        val commentDto = postCommentDtoMapper.mapFromDomain(comment)
        db.collection(POSTS).document(postId).collection(COMMENTS).add(commentDto)
    }

    /**
     * ========= Pagination =========
     */
    private var lastVisibleDocument: DocumentSnapshot? = null
    private var isEndOfList: Boolean = false

    private fun resetPagination() {
        lastVisibleDocument = null
        isEndOfList = false
    }

    override suspend fun fetchNextCommentsPage(postId: String): Resource<List<PostComment>, DataError.Firestore> {
        if (isEndOfList)
            return Resource.Failure(error = DataError.Firestore.DOCUMENT_NOT_FOUND) // TODO: Handle better

        val baseQuery = db.collection(POSTS)
            .document(postId)
            .collection(COMMENTS)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)

        val query = lastVisibleDocument?.let { baseQuery.startAfter(it).limit(COMMENT_PAGE_SIZE) }
            ?: baseQuery.limit(COMMENT_PAGE_SIZE)

        return firestoreUtils.queryDocuments(query, PostCommentDto::class.java)
            .map { pagingResult ->
                lastVisibleDocument = pagingResult.lastDocument

                if (pagingResult.objects.size < COMMENT_PAGE_SIZE)
                    isEndOfList = true

                pagingResult.objects.map(postCommentDtoMapper::mapToDomain)
            }
    }
}