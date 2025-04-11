package com.pegio.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val currentUserIdFlow = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override fun hasSavedAuthSession() = auth.currentUser != null
    override fun isAnonymousSession() = auth.currentUser?.isAnonymous == true
    override fun getCurrentUserId() = auth.currentUser?.uid
    override fun observeCurrentUserId() = currentUserIdFlow

    override suspend fun signInWithGoogle(idToken: String): Resource<Unit, DataError.Auth> {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        return try {
            auth.signInWithCredential(firebaseCredential).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToSignInError(e))
        }
    }

    override suspend fun signInAnonymously(): Resource<Unit, DataError.Auth> {
        return try {
            auth.signInAnonymously().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToSignInError(e))
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    private fun mapExceptionToSignInError(e: Exception): DataError.Auth {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> DataError.Auth.INVALID_CREDENTIAL
            is FirebaseAuthInvalidUserException -> DataError.Auth.INVALID_USER
            else -> DataError.Auth.UNKNOWN
        }
    }
}