package com.pegio.gymbro.data.remote.core

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUtils @Inject constructor() {

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
            Resource.Failure(error = mapExceptionToNetworkError(e))
        }
    }

    suspend fun <T> queryDocuments(query: Query, klass: Class<T>): Resource<List<T>, DataError.Firestore> {
        return try {
            val documentSnapshot = query.get().await()

            Resource.Success(data = documentSnapshot.toObjects(klass))

        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToNetworkError(e))
        }
    }

    private fun mapExceptionToNetworkError(e: Exception): DataError.Firestore {
        return when (e) {
            is FirebaseFirestoreException -> mapFirebaseCodeToError(e.code)
            else -> DataError.Firestore.UNKNOWN
        }
    }

    private fun mapFirebaseCodeToError(code: FirebaseFirestoreException.Code): DataError.Firestore {
        return when (code) {
            FirebaseFirestoreException.Code.CANCELLED -> DataError.Firestore.CANCELLED
            FirebaseFirestoreException.Code.UNKNOWN -> DataError.Firestore.UNKNOWN
            FirebaseFirestoreException.Code.INVALID_ARGUMENT -> DataError.Firestore.INVALID_ARGUMENT
            FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> DataError.Firestore.DEADLINE_EXCEEDED
            FirebaseFirestoreException.Code.NOT_FOUND -> DataError.Firestore.DOCUMENT_NOT_FOUND
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> DataError.Firestore.ALREADY_EXISTS
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> DataError.Firestore.PERMISSION_DENIED
            FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> DataError.Firestore.RESOURCE_EXHAUSTED
            FirebaseFirestoreException.Code.FAILED_PRECONDITION -> DataError.Firestore.FAILED_PRECONDITION
            FirebaseFirestoreException.Code.ABORTED -> DataError.Firestore.ABORTED
            FirebaseFirestoreException.Code.OUT_OF_RANGE -> DataError.Firestore.OUT_OF_RANGE
            FirebaseFirestoreException.Code.UNIMPLEMENTED -> DataError.Firestore.UNIMPLEMENTED
            FirebaseFirestoreException.Code.INTERNAL -> DataError.Firestore.INTERNAL
            FirebaseFirestoreException.Code.UNAVAILABLE -> DataError.Firestore.UNAVAILABLE
            FirebaseFirestoreException.Code.DATA_LOSS -> DataError.Firestore.DATA_LOSS
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> DataError.Firestore.UNAUTHENTICATED
            FirebaseFirestoreException.Code.OK -> DataError.Firestore.UNEXPECTED
        }
    }
}