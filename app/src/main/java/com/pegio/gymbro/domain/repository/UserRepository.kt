package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.User

interface UserRepository {
    fun getCurrentUserId(): String?
    suspend fun fetchUserById(id: String): Resource<User, DataError.Firestore>
    fun saveUser(user: User)
}