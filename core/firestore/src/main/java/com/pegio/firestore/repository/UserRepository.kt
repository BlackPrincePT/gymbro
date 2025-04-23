package com.pegio.firestore.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchUserById(id: String): Resource<User, DataError.Firestore>
    fun fetchUserSteamById(id: String): Flow<Resource<User, DataError.Firestore>>
    fun saveUser(user: User)
}