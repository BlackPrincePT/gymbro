package com.pegio.firestore.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.firestore.model.FirestorePagingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUtils @Inject internal constructor() {

    fun <T> observeDocument(documentRef: DocumentReference, klass: Class<T>): Flow<Resource<T, DataError.Firestore>> {
        return documentRef.snapshots()
            .map { documentSnapshot ->

                if (documentSnapshot.exists()) {
                    documentSnapshot.toObject(klass)?.let {
                        Resource.Success(data = it)
                    } ?: Resource.Failure(error = DataError.Firestore.DOCUMENT_PARSE_FAILED)
                } else {
                    Resource.Failure(error = DataError.Firestore.DOCUMENT_NOT_FOUND)
                }
            }
            .catch { cause: Throwable ->
                Resource.Failure(error = mapExceptionToFirestoreError(cause))
            }
    }

    fun <T> observeDocuments(query: Query, klass: Class<T>) : Flow<Resource<List<T>, DataError.Firestore>> {
        return query.snapshots()
            .map { querySnapshot ->
                Resource.Success(data = querySnapshot.toObjects(klass))
            }
            .catch { cause: Throwable ->
                Resource.Failure(error = mapExceptionToFirestoreError(cause)).also { println(cause) }
            }
    }

    suspend fun <T> readDocument(documentRef: DocumentReference, klass: Class<T>): Resource<T, DataError.Firestore> {
        return try {
            val documentSnapshot = documentRef.get().await()

            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(klass)?.let {
                    Resource.Success(data = it)
                } ?: Resource.Failure(error = DataError.Firestore.DOCUMENT_PARSE_FAILED)
            } else {
                Resource.Failure(error = DataError.Firestore.DOCUMENT_NOT_FOUND)
            }
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToFirestoreError(e))
        }
    }

    suspend fun <T> queryDocuments(query: Query, klass: Class<T>): Resource<FirestorePagingResult<T>, DataError.Firestore> {
        return try {
            val querySnapshot = query.get().await()

            val objects = querySnapshot.toObjects(klass)
            val lastDocument = querySnapshot.documents.lastOrNull()

            Resource.Success(data = FirestorePagingResult(objects, lastDocument))
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToFirestoreError(e))
        }
    }

    private fun mapExceptionToFirestoreError(e: Throwable): DataError.Firestore {
        return when (e) {
            is FirebaseFirestoreException -> mapFirebaseCodeToError(e.code)
            else -> DataError.Firestore.UNKNOWN
        }
    }

    private fun mapFirebaseCodeToError(code: FirebaseFirestoreException.Code): DataError.Firestore {
        return when (code) {
            FirebaseFirestoreException.Code.NOT_FOUND -> DataError.Firestore.DOCUMENT_NOT_FOUND
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> DataError.Firestore.PERMISSION_DENIED
            FirebaseFirestoreException.Code.INTERNAL -> DataError.Firestore.INTERNAL
            FirebaseFirestoreException.Code.UNAVAILABLE -> DataError.Firestore.UNAVAILABLE
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> DataError.Firestore.UNAUTHENTICATED
            else -> DataError.Firestore.UNKNOWN
        }
    }
}