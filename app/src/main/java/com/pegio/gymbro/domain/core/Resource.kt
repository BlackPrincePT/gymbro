package com.pegio.gymbro.domain.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

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

fun <D> Resource<D, *>.withData(action: (D?) -> Unit) {
    when (this) {
        is Resource.Success -> action(data)
        is Resource.Failure -> action(null)
    }
}

fun <E : Error> Resource<*, E>.withError(action: (E?) -> Unit) {
    when (this) {
        is Resource.Success -> action(null)
        is Resource.Failure -> action(error)
    }
}

fun <D, E : Error> Flow<Resource<D, E>>.onSuccess(action: suspend (D) -> Unit): Flow<Resource<D, E>> {
    return onEach { resource ->
        if (resource is Resource.Success)
            action(resource.data)
    }
}

fun <D, E : Error> Flow<Resource<D, E>>.onFailure(action: suspend (E) -> Unit): Flow<Resource<D, E>> {
    return onEach { resource ->
        if (resource is Resource.Failure)
            action(resource.error)
    }
}