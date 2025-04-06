package com.pegio.gymbro.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pegio.gymbro.data.remote.core.FirebaseConstants.POSTS
import com.pegio.gymbro.data.remote.core.FirebaseConstants.TIMESTAMP
import com.pegio.gymbro.data.remote.core.FirebaseConstants.UP_VOTES_IN_LAST_24_HOURS
import com.pegio.gymbro.data.remote.core.FirestoreUtils
import com.pegio.gymbro.data.remote.model.PostDto
import com.pegio.gymbro.data.remote.model.mapper.PostDtoMapper
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.map
import com.pegio.gymbro.domain.model.Post
import com.pegio.gymbro.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
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