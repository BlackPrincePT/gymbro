package com.pegio.gymbro.domain.core

sealed interface DataError : Error {

    enum class Network : DataError {
        UNKNOWN,
        TIMEOUT,
        NO_CONNECTION,
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        METHOD_NOT_ALLOWED,
        CONFLICT,
        INTERNAL_SERVER_ERROR,
        BAD_GATEWAY,
        SERVICE_UNAVAILABLE,
        GATEWAY_TIMEOUT
    }

    enum class FirebaseAuth : DataError {
        INVALID_CREDENTIAL,
        INVALID_USER,
        NETWORK_ERROR,
        FIREBASE_ERROR,
        UNKNOWN
    }

    enum class Firestore : DataError {
        CANCELLED,
        UNKNOWN,
        UNEXPECTED,
        INVALID_ARGUMENT,
        DEADLINE_EXCEEDED,
        DOCUMENT_NOT_FOUND,
        ALREADY_EXISTS,
        PERMISSION_DENIED,
        RESOURCE_EXHAUSTED,
        FAILED_PRECONDITION,
        ABORTED,
        OUT_OF_RANGE,
        UNIMPLEMENTED,
        INTERNAL,
        UNAVAILABLE,
        DATA_LOSS,
        UNAUTHENTICATED,
        DOCUMENT_PARSE_FAILED
    }
}