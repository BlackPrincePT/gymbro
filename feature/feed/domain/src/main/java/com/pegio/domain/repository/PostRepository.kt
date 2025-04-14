package com.pegio.domain.repository

import com.pegio.model.Post
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource

interface PostRepository {
    fun uploadPost(post: Post)
    suspend fun loadRelevantPosts(): Resource<List<Post>, DataError.Firestore>
}