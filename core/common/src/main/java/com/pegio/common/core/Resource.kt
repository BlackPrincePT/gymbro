package com.pegio.common.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Resource<out D, out E : Error> {
    data class Success<out D>(val data: D) : Resource<D, Nothing>
    data class Failure<out E : Error>(val error: E) : Resource<Nothing, E>
}


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


fun <D> D.asSuccess() = Resource.Success(data = this)
fun <E : Error> E.asFailure() = Resource.Failure(error = this)


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


@OptIn(ExperimentalContracts::class)
fun <D> Resource<D, *>.isSuccess(): Boolean {
    contract { returns(value = true) implies (this@isSuccess is Resource.Success<D>) }
    return this is Resource.Success
}

@OptIn(ExperimentalContracts::class)
fun <E : Error> Resource<*, E>.isFailure(): Boolean {
    contract { returns(value = true) implies (this@isFailure is Resource.Failure<E>) }
    return this is Resource.Failure
}

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


fun <D> Resource<D, *>.getOrNull() = (this as? Resource.Success)?.data
fun <E : Error> Resource<*, E>.errorOrNull() = (this as? Resource.Failure)?.error


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


inline fun <D, E : Error> Resource<D, E>.getOrElse(block: (E) -> Nothing) =
    when (this) {
        is Resource.Success -> data
        is Resource.Failure -> block(error)
    }

inline fun <D, E : Error> Resource<D, E>.errorOrElse(block: (D) -> Nothing) =
    when (this) {
        is Resource.Success -> block(data)
        is Resource.Failure -> error
    }


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


inline fun <T, D, E : Error> Resource<D, E>.map(transform: (D) -> T) =
    when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Failure -> Resource.Failure(error)
    }

inline fun <T, D, E : Error> Resource<List<D>, E>.mapList(transform: (D) -> T) =
    map { it.map(transform) }

inline fun <T, D, E : Error> Flow<Resource<D, E>>.convert(crossinline transform: (D) -> T) =
    map { it.map(transform) }

inline fun <T, D, E : Error> Flow<Resource<List<D>, E>>.convertList(crossinline transform: (D) -> T) =
    map { it.mapList(transform) }


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


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


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


suspend fun <D, E : Error> retryableCall(
    retries: Int = 3,
    initialDelay: Long = 500L,
    predicate: (Error) -> Boolean = { it is Retryable },
    block: suspend () -> Resource<D, E>
): Resource<D, E> {
    var attempt = 0

    while (true) {
        when (val result = block()) {
            is Resource.Success -> return result
            is Resource.Failure -> {
                if (attempt < retries && predicate(result.error)) delay(timeMillis = ++attempt * initialDelay)
                else return result
            }
        }
    }
}