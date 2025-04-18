package com.pegio.domain.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.PostComment
import org.w3c.dom.Comment

interface PostCommentRepository {

    // Upload
    fun writeComment(comment: PostComment, postId: String)

    // Fetch
    suspend fun fetchNextCommentsPage(postId: String): Resource<List<PostComment>, DataError.Firestore>
}