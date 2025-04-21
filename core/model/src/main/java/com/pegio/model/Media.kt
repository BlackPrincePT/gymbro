package com.pegio.model

data class Media(
    val url: String,
    val type: Type
) {

    enum class Type {
        IMAGE,
        VIDEO
    }
}