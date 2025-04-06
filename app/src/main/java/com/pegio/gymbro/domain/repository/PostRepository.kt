package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun observePostsPagingStream(): Flow<Resource<List<Post>, DataError.Firestore>>
    fun uploadPost(post: Post)
}