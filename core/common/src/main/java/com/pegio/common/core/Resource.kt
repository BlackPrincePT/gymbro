package com.pegio.common.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed interface Resource<out D, out E : Error> {
    data class Success<out D>(val data: D) : Resource<D, Nothing>
    data class Failure<out E : Error>(val error: E) : Resource<Nothing, E>
}

// ====== State ====== \\

inline val <D, E : Error> Resource<D, E>.isSuccess: Boolean
    get() = this is Resource.Success

inline val <D, E : Error> Resource<D, E>.isFailure: Boolean
    get() = this is Resource.Failure

// ====== Create ====== \\

fun <D> D.asResource() = Resource.Success(data = this)
fun <E : Error> E.asResource() = Resource.Failure(error = this)

// ====== Access ====== \\

fun <D> Resource<D, *>.get() = (this as Resource.Success).data
fun <D> Resource<D, *>.getOrNull() = (this as? Resource.Success)?.data
fun <D> Resource<D, *>.getOrElse(default: D) = getOrNull() ?: default

fun <E : Error> Resource<*, E>.error() = (this as Resource.Failure).error
fun <E : Error> Resource<*, E>.errorOrNull() = (this as? Resource.Failure)?.error
fun <E : Error> Resource<*, E>.errorOrElse(default: E) = errorOrNull() ?: default

// ====== Mapper ====== \\

fun <T, D, E : Error> Resource<D, E>.map(transform: (D) -> T): Resource<T, E> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Failure -> Resource.Failure(error)
    }
}

fun <T, D, E : Error> Resource<List<D>, E>.mapList(transform: (D) -> T): Resource<List<T>, E> {
    return map { list ->
        list.map(transform)
    }
}

fun <T, D, E : Error> Flow<Resource<D, E>>.convert(transform: (D) -> T): Flow<Resource<T, E>> {
    return map { resource ->
        resource.map(transform)
    }
}

fun <T, D, E : Error> Flow<Resource<List<D>, E>>.convertList(transform: (D) -> T): Flow<Resource<List<T>, E>> {
    return map { resource ->
        resource.mapList(transform)
    }
}

// ====== Handler ====== \\

inline fun <D, E : Error> Resource<D, E>.onSuccess(action: (D) -> Unit): Resource<D, E> {
    if (this is Resource.Success)
        action(data)

    return this
}

inline fun <D, E : Error> Resource<D, E>.onFailure(action: (E) -> Unit): Resource<D, E> {
    if (this is Resource.Failure)
        action(error)

    return this
}

suspend inline fun <D, E : Error> Resource<D, E>.onSuccessAsync(noinline action: suspend (D) -> Unit): Resource<D, E> {
    if (this is Resource.Success)
        action(data)

    return this
}

suspend inline fun <D, E : Error> Resource<D, E>.onFailureAsync(noinline action: suspend (E) -> Unit): Resource<D, E> {
    if (this is Resource.Failure)
        action(error)

    return this
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