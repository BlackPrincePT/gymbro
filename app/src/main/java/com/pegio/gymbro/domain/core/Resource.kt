package com.pegio.gymbro.domain.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface Resource<out D, out E : Error> {
    data class Success<out D>(val data: D) : Resource<D, Nothing>
    data class Failure<out E : Error>(val error: E) : Resource<Nothing, E>
}

fun <T, D, E : Error> Resource<D, E>.map(transform: (D) -> T): Resource<T, E> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Failure -> Resource.Failure(error)
    }
}

fun <T, D, E : Error> Flow<Resource<D, E>>.convert(transform: (D) -> T): Flow<Resource<T, E>> {
    return map { resource ->
        resource.map(transform)
    }
}

fun <D, E : Error> Resource<D, E>.onSuccess(action: (D) -> Unit): Resource<D, E> {
    if (this is Resource.Success)
        action(data)

    return this
}

fun <D, E : Error> Resource<D, E>.onFailure(action: (E) -> Unit): Resource<D, E> {
    if (this is Resource.Failure)
        action(error)

    return this
}

fun <D> Resource<D, *>.ifSuccess(update: (D?) -> Unit) {
    when (this) {
        is Resource.Success -> update(data)
        is Resource.Failure -> update(null)
    }
}

fun <E : Error> Resource<*, E>.ifFailure(update: (E?) -> Unit) {
    when (this) {
        is Resource.Success -> update(null)
        is Resource.Failure -> update(error)
    }
}