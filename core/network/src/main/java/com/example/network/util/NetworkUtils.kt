package com.example.network.util

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkUtils @Inject constructor() {

    suspend fun <T> handleHttpRequest(apiCall: suspend () -> Response<T>): Resource<T, DataError.Network> {
        return try {
            val response = apiCall.invoke()

            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(data = it) }
                    ?: Resource.Failure(error = DataError.Network.UNKNOWN)
            } else {
                val error = mapResponseCodeToError(response.code())
                Resource.Failure(error = error)
            }
        } catch (e: Exception) {
            Resource.Failure(error = mapExceptionToNetworkError(e))
        }
    }

    private fun mapResponseCodeToError(code: Int): DataError.Network {
        return when (code) {
            400 -> DataError.Network.BAD_REQUEST
            401 -> DataError.Network.UNAUTHORIZED
            403 -> DataError.Network.FORBIDDEN
            404 -> DataError.Network.NOT_FOUND
            408 -> DataError.Network.TIMEOUT
            422 -> DataError.Network.BAD_REQUEST
            500 -> DataError.Network.INTERNAL_SERVER_ERROR
            503 -> DataError.Network.SERVICE_UNAVAILABLE
            else -> DataError.Network.UNKNOWN
        }
    }

    private fun mapExceptionToNetworkError(e: Exception): DataError.Network {
        return when (e) {
            is HttpException -> mapResponseCodeToError(e.code())
            is SocketTimeoutException -> DataError.Network.TIMEOUT
            is UnknownHostException -> DataError.Network.NO_CONNECTION
            is ConnectException -> DataError.Network.NO_CONNECTION
            is IOException -> DataError.Network.NO_CONNECTION
            else -> DataError.Network.UNKNOWN
        }
    }
}