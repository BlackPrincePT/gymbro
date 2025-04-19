package com.pegio.auth.repository

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.domain.repository.AuthRepository
import com.pegio.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val getCredentialRequest: GetCredentialRequest
) : AuthRepository {

    private val currentUserFlow = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun getCurrentUser(): User.Auth? {
        return auth.currentUser?.run { User.Auth(id = uid, isAnonymous = isAnonymous) }
    }

    override fun getCurrentUserStream(): Flow<User.Auth?> {
        return currentUserFlow.map { user ->
            user?.run { User.Auth(id = uid, isAnonymous = isAnonymous) }
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    // ========= Sign in anonymously ========= \\

    override suspend fun signInAnonymously(): Resource<Unit, DataError.Auth> {
        return try {
            auth.signInAnonymously().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToSignInError(e))
        }
    }

    // ========= Sign in with Google ========= \\

    override suspend fun launchGoogleAuthOptions(context: Context): Resource<Unit, DataError.Auth> {
        return try {
            val result = CredentialManager.create(context)
                .getCredential(request = getCredentialRequest, context = context)

            createTokenWithCredentials(result.credential)

        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToSignInError(e))
        }
    }

    private suspend fun createTokenWithCredentials(credential: Credential): Resource<Unit, DataError.Auth> {
        return if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            signInWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Resource.Failure(error = DataError.Auth.INVALID_CREDENTIAL)
        }
    }

    private suspend fun signInWithGoogle(idToken: String): Resource<Unit, DataError.Auth> {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        return try {
            auth.signInWithCredential(firebaseCredential).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToSignInError(e))
        }
    }

    private fun mapExceptionToSignInError(e: Exception): DataError.Auth {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> DataError.Auth.INVALID_CREDENTIAL
            is FirebaseAuthInvalidUserException -> DataError.Auth.INVALID_USER
            is CancellationException -> DataError.Auth.CANCELLED
            else -> DataError.Auth.UNKNOWN
        }
    }
}