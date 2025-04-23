package com.pegio.model

data class FollowRecord(
    val userId: String
) {

    enum class Type {
        FOLLOWERS,
        FOLLOWING
    }
}