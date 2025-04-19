package com.pegio.feed.presentation.model

import com.pegio.common.presentation.model.UiUser

data class UiPostComment(
    val id: String,
    val author: UiUser,
    val content: String,
    val date: String
) {

    companion object {

        val DEFAULT = UiPostComment(
            id = "",
            author = UiUser.DEFAULT,
            content = "In the morning if my face is a little puffy I'll put on an ice pack while doing stomach crunches.",
            date = "2 hrs"
        )
    }
}