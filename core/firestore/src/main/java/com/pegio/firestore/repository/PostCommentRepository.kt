package com.pegio.firestore.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.PostComment

interface PostCommentRepository {

    fun resetPagination()

    // Upload
    fun writeComment(comment: PostComment, postId: String)

    // Fetch
    suspend fun fetchNextCommentsPage(postId: String): Resource<List<PostComment>, DataError>
}