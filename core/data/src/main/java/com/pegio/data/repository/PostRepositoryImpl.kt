package com.pegio.data.repository

import com.pegio.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.domain.repository.PostRepository
import com.pegio.firestore.core.FirestoreConstants.POSTS
import com.pegio.firestore.core.FirestoreConstants.TIMESTAMP
import com.pegio.firestore.core.FirestoreConstants.UP_VOTES_IN_LAST_24_HOURS
import com.pegio.firestore.model.PostDto
import com.pegio.firestore.model.mapper.PostDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import javax.inject.Inject

internal class PostRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val postDtoMapper: PostDtoMapper
) : PostRepository {

    companion object {
        private const val POST_PAGE_SIZE: Long = 20L
    }

    private var lastVisibleDocument: DocumentSnapshot? = null
    private var isEndOfList: Boolean = false

    private fun resetPagination() {
        lastVisibleDocument = null
        isEndOfList = false
    }

    override fun uploadPost(post: Post) {
        val postDto = postDtoMapper.mapFromDomain(post)
        db.collection(POSTS).add(postDto)
    }

    override suspend fun loadRelevantPosts(): Resource<List<Post>, DataError.Firestore> {
        if (isEndOfList)
            return Resource.Failure(error = DataError.Firestore.DOCUMENT_NOT_FOUND) // TODO: Handle better

        val baseQuery = db.collection(POSTS)
            .orderBy(UP_VOTES_IN_LAST_24_HOURS, Query.Direction.DESCENDING)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)

        val query = lastVisibleDocument?.let { baseQuery.startAfter(it).limit(POST_PAGE_SIZE) }
            ?: baseQuery.limit(POST_PAGE_SIZE)

        return firestoreUtils.queryDocuments(query, PostDto::class.java)
            .map { pagingResult ->
                lastVisibleDocument = pagingResult.lastDocument

                if (pagingResult.objects.size < POST_PAGE_SIZE)
                    isEndOfList = true

                pagingResult.objects.map(postDtoMapper::mapToDomain)
            }
    }
}