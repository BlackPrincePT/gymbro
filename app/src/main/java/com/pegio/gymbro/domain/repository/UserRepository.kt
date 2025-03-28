package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchUser(id: String): Resource<User, DataError.Firestore>
    fun fetchUserSteam(id: String): Flow<Resource<User, DataError.Firestore>>
    fun saveUser(user: User)
}