package com.pegio.gymbro.presentation.core

import com.pegio.gymbro.R
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Error

fun Error.toStringResId(): Int = when (this) {
    is DataError -> this.toStringResId()
}

private fun DataError.toStringResId(): Int = when (this) {
    is DataError.Network -> this.toStringResId()
    is DataError.Firestore -> this.toStringResId()
    is DataError.FirebaseAuth -> this.toStringResId()
    is DataError.CloudStorage -> this.toStringResId()
}

private fun DataError.Network.toStringResId(): Int = when (this) {
    DataError.Network.UNKNOWN -> R.string.error_unknown
    DataError.Network.TIMEOUT -> R.string.error_timeout
    DataError.Network.NO_CONNECTION -> R.string.error_no_connection
    DataError.Network.BAD_REQUEST -> R.string.error_bad_request
    DataError.Network.UNAUTHORIZED -> R.string.error_unauthorized
    DataError.Network.FORBIDDEN -> R.string.error_forbidden
    DataError.Network.NOT_FOUND -> R.string.error_not_found
    DataError.Network.METHOD_NOT_ALLOWED -> R.string.error_method_not_allowed
    DataError.Network.CONFLICT -> R.string.error_conflict
    DataError.Network.INTERNAL_SERVER_ERROR -> R.string.error_internal_server_error
    DataError.Network.BAD_GATEWAY -> R.string.error_bad_gateway
    DataError.Network.SERVICE_UNAVAILABLE -> R.string.error_service_unavailable
    DataError.Network.GATEWAY_TIMEOUT -> R.string.error_gateway_timeout
}

private fun DataError.Firestore.toStringResId(): Int = when (this) {
    DataError.Firestore.CANCELLED -> R.string.firestore_cancelled
    DataError.Firestore.UNKNOWN -> R.string.error_unknown
    DataError.Firestore.UNEXPECTED -> R.string.firestore_unexpected
    DataError.Firestore.INVALID_ARGUMENT -> R.string.firestore_invalid_argument
    DataError.Firestore.DEADLINE_EXCEEDED -> R.string.firestore_deadline_exceeded
    DataError.Firestore.DOCUMENT_NOT_FOUND -> R.string.firestore_document_not_found
    DataError.Firestore.ALREADY_EXISTS -> R.string.firestore_already_exists
    DataError.Firestore.PERMISSION_DENIED -> R.string.firestore_permission_denied
    DataError.Firestore.RESOURCE_EXHAUSTED -> R.string.firestore_resource_exhausted
    DataError.Firestore.FAILED_PRECONDITION -> R.string.firestore_failed_precondition
    DataError.Firestore.ABORTED -> R.string.firestore_aborted
    DataError.Firestore.OUT_OF_RANGE -> R.string.firestore_out_of_range
    DataError.Firestore.UNIMPLEMENTED -> R.string.firestore_unimplemented
    DataError.Firestore.INTERNAL -> R.string.firestore_internal
    DataError.Firestore.UNAVAILABLE -> R.string.firestore_unavailable
    DataError.Firestore.DATA_LOSS -> R.string.firestore_data_loss
    DataError.Firestore.UNAUTHENTICATED -> R.string.firestore_unauthenticated
    DataError.Firestore.DOCUMENT_PARSE_FAILED -> R.string.firestore_document_parse_failed
}

private fun DataError.FirebaseAuth.toStringResId(): Int {
    return when (this) {
        DataError.FirebaseAuth.INVALID_CREDENTIAL -> R.string.error_invalid_credential
        DataError.FirebaseAuth.INVALID_USER -> R.string.error_invalid_user
        DataError.FirebaseAuth.NETWORK_ERROR -> R.string.error_network
        DataError.FirebaseAuth.FIREBASE_ERROR -> R.string.error_firebase
        DataError.FirebaseAuth.UNKNOWN -> R.string.error_unknown
    }
}