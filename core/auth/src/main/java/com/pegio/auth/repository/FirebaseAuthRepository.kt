package com.pegio.auth.repository

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
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

    override fun getCurrentUserStream(): Flow<User.Auth?> {
        return currentUserFlow.map { user ->
            user?.run {
                User.Auth(id = uid, isAnonymous = isAnonymous)
            }
        }
    }

    override fun getCurrentUser(): User.Auth? {
        return auth.currentUser?.run { User.Auth(id = uid, isAnonymous = isAnonymous) }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun signOut() = auth.signOut()


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override suspend fun launchGoogleAuthOptionsAndCreateToken(context: Context): Resource<String, DataError.Auth> {
        return try {
            val result = CredentialManager.create(context)
                .getCredential(request = getCredentialRequest, context = context)

            createTokenWithCredentials(result.credential)
        } catch (e: Exception) {
            mapExceptionToSignInError(e).asFailure()
        }
    }

    private fun createTokenWithCredentials(credential: Credential): Resource<String, DataError.Auth> {
        return if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            googleIdTokenCredential.idToken.asSuccess()
        } else {
            DataError.Auth.InvalidCredential.asFailure()
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override suspend fun signInWithGoogle(token: String): Resource<Unit, DataError.Auth> {
        return try {
            val authCredential = GoogleAuthProvider.getCredential(token, null)

            auth.signInWithCredential(authCredential).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToSignInError(e))
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override suspend fun signInAnonymously(): Resource<Unit, DataError.Auth> {
        return try {
            auth.signInAnonymously().await()
            Unit.asSuccess()
        } catch (e: Exception) {
            mapExceptionToSignInError(e).asFailure()
        }
    }

    override suspend fun linkAnonymousAccount(token: String): Resource<Unit, DataError.Auth> {
        val currentUser = auth.currentUser ?: return DataError.Auth.Unauthenticated.asFailure()

        if (!currentUser.isAnonymous)
            return DataError.Auth.AccountAlreadyExists.asFailure()

        return try {
            val authCredential = GoogleAuthProvider.getCredential(token, null)
            currentUser.linkWithCredential(authCredential).await()

            Unit.asSuccess()
        } catch (e: Exception) {
            mapExceptionToSignInError(e).asFailure()
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun mapExceptionToSignInError(e: Exception): DataError.Auth {
        return when (e) {
            is FirebaseAuthUserCollisionException -> DataError.Auth.AccountAlreadyExists
            is CancellationException -> DataError.Auth.Cancelled
            else -> DataError.Auth.Unknown
        }
    }
}