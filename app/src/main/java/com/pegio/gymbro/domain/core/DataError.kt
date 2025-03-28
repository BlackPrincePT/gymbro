package com.pegio.gymbro.domain.core

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