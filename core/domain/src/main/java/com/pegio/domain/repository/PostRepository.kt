package com.pegio.domain.repository

import com.pegio.model.Post
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource

interface PostRepository {

    // Upload
    fun uploadPost(post: Post)

    // Fetch
    suspend fun fetchPostById(id: String): Resource<Post, DataError.Firestore>
    suspend fun fetchNextRelevantPostsPage(): Resource<List<Post>, DataError>
    fun resetPagination()
}