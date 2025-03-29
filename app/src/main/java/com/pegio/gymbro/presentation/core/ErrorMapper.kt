package com.pegio.gymbro.presentation.core

import com.pegio.gymbro.R
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Error
import com.pegio.gymbro.domain.core.ValidationError

fun Error.toStringResId(): Int = when (this) {
    is DataError -> this.toStringResId()
    is ValidationError -> this.toStringResId()
}

private fun DataError.toStringResId(): Int = when (this) {
    is DataError.Network -> this.toStringResId()
    is DataError.Auth -> this.toStringResId()
    is DataError.Firestore -> this.toStringResId()
}

private fun ValidationError.toStringResId(): Int = when (this) {
    is ValidationError.Username -> this.toStringResId()
    is ValidationError.Age -> this.toStringResId()
    is ValidationError.Gender -> this.toStringResId()
    is ValidationError.Height -> this.toStringResId()
    is ValidationError.Weight -> this.toStringResId()
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

private fun ValidationError.Username.toStringResId(): Int = when (this) {
    ValidationError.Username.EMPTY -> R.string.error_username_empty
    ValidationError.Username.TOO_SHORT -> R.string.error_username_too_short
}

private fun ValidationError.Age.toStringResId(): Int = when (this) {
    ValidationError.Age.INVALID -> R.string.error_age_invalid
    ValidationError.Age.TOO_YOUNG -> R.string.error_age_too_young
}

private fun ValidationError.Gender.toStringResId(): Int = when (this) {
    ValidationError.Gender.EMPTY -> R.string.error_gender_empty
    ValidationError.Gender.INVALID -> R.string.error_gender_invalid
}

private fun ValidationError.Height.toStringResId(): Int = when (this) {
    ValidationError.Height.INVALID -> R.string.error_height_invalid
    ValidationError.Height.TOO_LOW -> R.string.error_height_too_low
}

private fun ValidationError.Weight.toStringResId(): Int = when (this) {
    ValidationError.Weight.INVALID -> R.string.error_weight_invalid
    ValidationError.Weight.TOO_LOW -> R.string.error_weight_too_low
}