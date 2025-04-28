package com.pegio.common.core

interface Error
interface Retryable
interface Displayable

sealed interface SessionError : Error {
    data object Unauthenticated : SessionError
    data object RegistrationIncomplete : SessionError
    data object AnonymousUser : SessionError, Displayable
    data object Unknown : SessionError, Retryable, Displayable
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

    sealed interface Firestore : DataError {
        data object DocumentNotFound : Firestore
        data object PermissionDenied : Firestore
        data object Unauthenticated : Firestore
        data object Internal : Firestore
        data object Unavailable : Firestore
        data object Unknown : Firestore
    }

    enum class Pagination : DataError {
        END_OF_PAGINATION_REACHED
    }
}

sealed interface ValidationError : Error {

    enum class Username : ValidationError, Displayable {
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

sealed interface WorkoutValidationError : Error {

    enum class WorkoutTitle : WorkoutValidationError{
        EMPTY, // When the workout title is blank
        TOO_SHORT,  // When the title is too short
        TOO_LONG // When the title is too long
    }

    enum class WorkoutDescription : WorkoutValidationError{
        EMPTY, // When the workout description is blank
        TOO_SHORT,  // When the description is too short
        TOO_LONG // When the description is too long
    }

    enum class Name : WorkoutValidationError {
        EMPTY,          // When the workout name is blank
        TOO_SHORT       // When the name is too short
    }

    enum class Description : WorkoutValidationError {
        EMPTY,          // When the description is blank
        TOO_SHORT       // When the description is too short
    }

    enum class Value : WorkoutValidationError {
        INVALID,        // When the value (e.g., reps or duration) is non-positive
        TOO_LOW         // When the value is below a logical threshold
    }

    enum class Sets : WorkoutValidationError {
        INVALID,        // When sets are non-positive
        TOO_MANY        // When sets exceed a reasonable upper limit
    }

    enum class MuscleGroups : WorkoutValidationError {
        EMPTY           // When no muscle group is selected
    }

    enum class WorkoutImage : WorkoutValidationError {
        EMPTY,          // When no image is provided
    }

    enum class Workouts : WorkoutValidationError {
        TOO_FEW,         // When workouts size is fewer than 5
        TOO_MANY         // When workouts size is more than 30
    }
}
