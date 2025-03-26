package com.pegio.gymbro.data.repository

import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override fun hasSavedAuthSession() = FirebaseAuth.getInstance().currentUser != null
    override fun isAnonymousSession() = FirebaseAuth.getInstance().currentUser?.isAnonymous == true

    override suspend fun signInWithGoogle(idToken: String): Resource<Unit, DataError.FirebaseAuth> {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        return try {
            Firebase.auth.signInWithCredential(firebaseCredential).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToSignInError(e))
        }
    }

    override suspend fun signInAnonymously(): Resource<Unit, DataError.FirebaseAuth> {
        return try {
            Firebase.auth.signInAnonymously().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToSignInError(e))
        }
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }

    private fun mapExceptionToSignInError(e: Exception): DataError.FirebaseAuth {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> DataError.FirebaseAuth.INVALID_CREDENTIAL
            is FirebaseAuthInvalidUserException -> DataError.FirebaseAuth.INVALID_USER
            is FirebaseNetworkException -> DataError.FirebaseAuth.NETWORK_ERROR
            is FirebaseException -> DataError.FirebaseAuth.FIREBASE_ERROR
            else -> DataError.FirebaseAuth.UNKNOWN
        }
    }
}