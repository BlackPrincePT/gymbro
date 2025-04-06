package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.Post

interface PostRepository {
    fun uploadPost(post: Post)
    suspend fun loadRelevantPosts(): Resource<List<Post>, DataError.Firestore>
}