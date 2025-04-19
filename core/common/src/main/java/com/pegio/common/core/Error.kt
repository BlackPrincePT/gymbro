package com.pegio.common.core

interface Error
interface Retryable

sealed interface SessionError : Error {
    data object Unauthenticated : SessionError, Retryable
    data object RegistrationIncomplete : SessionError, Retryable
    data object Unknown: SessionError
}

sealed interface DataError : Error {

    enum class Network : DataError {
        NO_CONNECTION,          // Show a message like "No internet connection."
        TIMEOUT,                // Inform the user the request took too long.
        BAD_REQUEST,            // Useful for validation errors.
        UNAUTHORIZED,           // Prompt for re-login or permission issues.
        FORBIDDEN,              // Notify the user they lack permissions.
        NOT_FOUND,              // Indicate that a resource could not be found.
        INTERNAL_SERVER_ERROR,  // Display a generic error message.
        SERVICE_UNAVAILABLE,    // Inform the user that the service is temporarily down.
        UNKNOWN                 // Catch-all for unspecified network errors.
    }

    enum class Auth : DataError {
        INVALID_CREDENTIAL,     // Inform the user their credentials are incorrect.
        INVALID_USER,           // Alert about issues with the user account.
        UNAUTHENTICATED,        // Force re-authentication.
        CANCELLED,              // Google options cancelled.
        UNKNOWN                 // A fallback for any Firebase Auth errors not explicitly handled.
    }

    enum class Firestore : DataError {
        DOCUMENT_NOT_FOUND,     // Typically handled by showing a "not found" UI.
        DOCUMENT_PARSE_FAILED,  // Handle illegal document model.
        PERMISSION_DENIED,      // Show a message about lacking access rights.
        UNAUTHENTICATED,        // Handle re-authentication if needed.
        INTERNAL,               // Display a generic error message.
        UNAVAILABLE,            // Alert the user about connectivity or server issues.
        UNKNOWN                 // For any other Firestore errors not explicitly caught.
    }
}

sealed interface ValidationError : Error {

    enum class Username : ValidationError {
        EMPTY,      // When the username is blank
        TOO_SHORT   // When the username is shorter than required
    }

    enum class Age : ValidationError {
        INVALID,    // When the age is less than or equal to 0
        TOO_YOUNG   // When the age is less than the minimum allowed (e.g., 18)
    }

    enum class Gender : ValidationError {
        EMPTY,      // When no gender is provided
        INVALID     // When the provided gender is not in the allowed list
    }

    enum class Height : ValidationError {
        INVALID,    // When the height value is nonsensical (e.g., negative or 0)
        TOO_LOW     // When the height is below a certain acceptable threshold
    }

    enum class Weight : ValidationError {
        INVALID,    // When the weight value is nonsensical
        TOO_LOW     // When the weight is below a certain acceptable threshold
    }
}