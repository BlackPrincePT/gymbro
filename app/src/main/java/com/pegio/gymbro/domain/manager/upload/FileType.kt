package com.pegio.gymbro.domain.manager.upload

enum class FileType {
    JPG;

    val extension: String
        get() = when (this) {
            JPG -> ".jpg"
        }
}