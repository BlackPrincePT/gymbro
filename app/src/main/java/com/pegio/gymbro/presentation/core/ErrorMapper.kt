package com.pegio.gymbro.presentation.core

import com.pegio.gymbro.R
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Error
fun Error.toStringResId(): Int = when (this) {
    is DataError -> this.toStringResId()
}

private fun DataError.toStringResId(): Int = when (this) {
    is DataError.Network -> this.toStringResId()
    is DataError.Auth -> this.toStringResId()
    is DataError.Firestore -> this.toStringResId()
}

private fun DataError.Network.toStringResId(): Int = when (this) {
    DataError.Network.NO_CONNECTION -> R.string.error_no_connection
    DataError.Network.TIMEOUT -> R.string.error_timeout
    DataError.Network.UNAUTHORIZED -> R.string.error_unauthorized
    DataError.Network.FORBIDDEN -> R.string.error_forbidden
    DataError.Network.NOT_FOUND -> R.string.error_not_found
    else -> R.string.error_generic
}

private fun DataError.Auth.toStringResId(): Int = when (this) {
    DataError.Auth.INVALID_CREDENTIAL -> R.string.error_invalid_credential
    DataError.Auth.INVALID_USER -> R.string.error_invalid_user
    DataError.Auth.UNAUTHENTICATED -> R.string.error_unauthenticated
    else -> R.string.error_generic
}

private fun DataError.Firestore.toStringResId(): Int = when (this) {
    DataError.Firestore.DOCUMENT_NOT_FOUND -> R.string.error_document_not_found
    DataError.Firestore.PERMISSION_DENIED -> R.string.error_permission_denied
    DataError.Firestore.UNAUTHENTICATED -> R.string.error_unauthenticated
    else -> R.string.error_generic
}
