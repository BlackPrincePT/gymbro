package com.pegio.firestore.core

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.firestore.util.FirestoreUtils

internal class FirestorePagingSource<T>(
    private val pageSize: Long,
    private val klass: Class<T>,
    private val firestoreUtils: FirestoreUtils
) {

    private var lastVisibleDocument: DocumentSnapshot? = null
    private var isEndOfList: Boolean = false

    fun resetPagination() {
        lastVisibleDocument = null
        isEndOfList = false
    }

    suspend fun loadNextPage(baseQuery: Query): Resource<List<T>, DataError> {
        if (isEndOfList)
            return Resource.Failure(error = DataError.Pagination.END_OF_PAGINATION_REACHED)

        val query = lastVisibleDocument?.let { baseQuery.startAfter(it).limit(pageSize) }
            ?: baseQuery.limit(pageSize)

        return firestoreUtils.queryDocuments(query, klass)
            .map { pagingResult ->
                lastVisibleDocument = pagingResult.lastDocument

                if (pagingResult.objects.size < pageSize)
                    isEndOfList = true

                pagingResult.objects
            }
    }
}