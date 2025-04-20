package com.pegio.firestore.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.firestore.core.FirestorePagingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirestoreUtils @Inject constructor() {

    fun <T> observeDocument(documentRef: DocumentReference, klass: Class<T>): Flow<Resource<T, DataError.Firestore>> {
        return documentRef.snapshots()
            .map { documentSnapshot ->
                documentSnapshot.toObject(klass)?.asSuccess()
                    ?: DataError.Firestore.DocumentNotFound.asFailure()
            }
            .catch { cause: Throwable ->
                mapExceptionToFirestoreError(cause).asFailure()
            }
    }

    fun <T> observeDocuments(query: Query, klass: Class<T>): Flow<Resource<List<T>, DataError.Firestore>> {
        return query.snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(klass).asSuccess()
            }
            .catch { cause: Throwable ->
                mapExceptionToFirestoreError(cause).asFailure()
            }
    }

    suspend fun <T> readDocument(documentRef: DocumentReference, klass: Class<T>): Resource<T, DataError.Firestore> {
        return try {
            val documentSnapshot = documentRef.get().await()

            documentSnapshot.toObject(klass)?.asSuccess()
                ?: DataError.Firestore.DocumentNotFound.asFailure()
        } catch (e: Exception) {
            mapExceptionToFirestoreError(e).asFailure()
        }
    }

    suspend fun <T> queryDocuments(query: Query, klass: Class<T>): Resource<FirestorePagingResult<T>, DataError.Firestore> {
        return try {
            val querySnapshot = query.get().await()

            val objects = querySnapshot.toObjects(klass)
            val lastDocument = querySnapshot.documents.lastOrNull()

            FirestorePagingResult(objects, lastDocument).asSuccess()
        } catch (e: Exception) {
            println(e)
            mapExceptionToFirestoreError(e).asFailure()
        }
    }

    private fun mapExceptionToFirestoreError(e: Throwable): DataError.Firestore {
        return when (e) {
            is FirebaseFirestoreException -> mapFirebaseCodeToError(e)
            else -> DataError.Firestore.Unknown
        }
    }

    private fun mapFirebaseCodeToError(exception: FirebaseFirestoreException): DataError.Firestore {
        return when (exception.code) {
            FirebaseFirestoreException.Code.NOT_FOUND -> DataError.Firestore.DocumentNotFound
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> DataError.Firestore.PermissionDenied
            FirebaseFirestoreException.Code.INTERNAL -> DataError.Firestore.Internal
            FirebaseFirestoreException.Code.UNAVAILABLE -> DataError.Firestore.Unavailable
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> DataError.Firestore.Unauthenticated
            else -> DataError.Firestore.Unknown
        }
    }
}