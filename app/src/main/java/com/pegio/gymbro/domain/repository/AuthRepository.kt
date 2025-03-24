package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Resource<Unit, DataError.Firebase>
    fun hasSavedAuthSession(): Boolean
    fun signOut()
}