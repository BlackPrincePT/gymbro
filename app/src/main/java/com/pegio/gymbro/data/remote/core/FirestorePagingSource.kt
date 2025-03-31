package com.pegio.gymbro.data.remote.core

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.pegio.gymbro.domain.core.ToDomainMapper
import kotlinx.coroutines.tasks.await

class FirestorePagingSource<Dto, Domain : Any>(
    private val query: Query,
    private val klass: Class<Dto>,
    private val mapper: ToDomainMapper<Dto, Domain>
) : PagingSource<QuerySnapshot, Domain>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Domain>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Domain> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleObject = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisibleObject).get().await()

            val dtoData = currentPage.toObjects(klass)

            LoadResult.Page(
                data = dtoData.map(mapper::mapToDomain),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}