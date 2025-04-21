package com.pegio.firestore.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.common.core.mapList
import com.pegio.domain.repository.PostRepository
import com.pegio.firestore.core.FirestoreConstants.AUTHOR_ID
import com.pegio.firestore.core.FirestoreConstants.POSTS
import com.pegio.firestore.core.FirestoreConstants.TIMESTAMP
import com.pegio.firestore.core.FirestoreConstants.UP_VOTES_IN_LAST_24_HOURS
import com.pegio.firestore.core.FirestorePagingSource
import com.pegio.firestore.model.PostDto
import com.pegio.firestore.model.mapper.PostDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.Post
import javax.inject.Inject

internal class PostRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val postDtoMapper: PostDtoMapper
) : PostRepository {

    private companion object {
        const val POST_PAGE_SIZE: Long = 10L
    }

    private val postsPagingSource =
        FirestorePagingSource(POST_PAGE_SIZE, PostDto::class.java, firestoreUtils)

    override fun resetPagination() {
        postsPagingSource.resetPagination()
    }

    override fun uploadPost(post: Post) {
        val postDto = postDtoMapper.mapFromDomain(post)
        db.collection(POSTS).add(postDto)
    }

    override suspend fun fetchPostById(id: String): Resource<Post, DataError.Firestore> {
        val documentRef = db.collection(POSTS).document(id)

        val result = firestoreUtils.readDocument(documentRef, PostDto::class.java)

        return result.map(postDtoMapper::mapToDomain)
    }

    override suspend fun fetchNextRelevantPostsPage(): Resource<List<Post>, DataError> {
        val baseQuery = db.collection(POSTS)
            .orderBy(UP_VOTES_IN_LAST_24_HOURS, Query.Direction.DESCENDING)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)

        return postsPagingSource.loadNextPage(baseQuery)
            .mapList(postDtoMapper::mapToDomain)
    }

    override suspend fun fetchLatestUserPostsPage(authorId: String): Resource<List<Post>, DataError> {
        val baseQuery = db.collection(POSTS)
            .whereEqualTo(AUTHOR_ID, authorId)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)

        return postsPagingSource.loadNextPage(baseQuery)
            .mapList(postDtoMapper::mapToDomain)
    }
}