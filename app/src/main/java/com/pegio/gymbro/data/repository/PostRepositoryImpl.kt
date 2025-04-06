package com.pegio.gymbro.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.pegio.gymbro.data.remote.core.FirebaseConstants.POSTS
import com.pegio.gymbro.data.remote.core.FirebaseConstants.TIMESTAMP
import com.pegio.gymbro.data.remote.core.FirebaseConstants.UP_VOTES_IN_LAST_24_HOURS
import com.pegio.gymbro.data.remote.core.FirestoreUtils
import com.pegio.gymbro.data.remote.model.PostDto
import com.pegio.gymbro.data.remote.model.mapper.PostDtoMapper
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.convert
import com.pegio.gymbro.domain.core.convertList
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.model.Post
import com.pegio.gymbro.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDtoMapper: PostDtoMapper,
    private val firestoreUtils: FirestoreUtils
) : PostRepository {

    private val db = Firebase.firestore

    private var lastVisibleTimestamp: Long? = Long.MAX_VALUE

    companion object {
        private const val POST_PAGE_SIZE: Long = 20L
    }

    override fun uploadPost(post: Post) {
        val postDto = postDtoMapper.mapFromDomain(post)
        db.collection(POSTS).add(postDto)
    }

    override fun observePostsPagingStream(): Flow<Resource<List<Post>, DataError.Firestore>> {
        val query = db.collection(POSTS)
            .orderBy(UP_VOTES_IN_LAST_24_HOURS, Query.Direction.DESCENDING)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
            .startAfter(lastVisibleTimestamp)
            .limit(POST_PAGE_SIZE)

        return firestoreUtils.observeDocuments(query, PostDto::class.java)
            .onSuccess { lastVisibleTimestamp = it.lastOrNull()?.timestamp }
            .convertList(postDtoMapper::mapToDomain)
    }
}