package com.pegio.common.presentation.util

import com.pegio.common.R
import com.pegio.common.core.DataError
import com.pegio.common.core.Error
import com.pegio.common.core.SessionError
import com.pegio.common.core.ValidationError
import com.pegio.common.core.WorkoutValidationError

fun Error.toStringResId(): Int = when (this) {
    is DataError -> this.toStringResId()
    is ValidationError -> this.toStringResId()
    is WorkoutValidationError -> this.toStringResId()
    is SessionError -> this.toStringResId()
    else -> R.string.feature_common_presentation_error_generic
}

private fun SessionError.toStringResId(): Int = when (this) {
    SessionError.AnonymousUser -> R.string.feature_common_you_aren_t_logged_in
    else -> R.string.feature_common_presentation_error_generic
}

private fun DataError.toStringResId(): Int = when (this) {
    is DataError.Network -> this.toStringResId()
    is DataError.Auth -> this.toStringResId()
    is DataError.Firestore -> this.toStringResId()
    else -> R.string.feature_common_presentation_error_generic
}

private fun ValidationError.toStringResId(): Int = when (this) {
    is ValidationError.Username -> this.toStringResId()
    is ValidationError.Age -> this.toStringResId()
    is ValidationError.Gender -> this.toStringResId()
    is ValidationError.Height -> this.toStringResId()
    is ValidationError.Weight -> this.toStringResId()
}

private fun DataError.Network.toStringResId(): Int = when (this) {
    DataError.Network.NO_CONNECTION -> R.string.feature_common_presentation_error_no_connection
    DataError.Network.TIMEOUT -> R.string.feature_common_presentation_error_timeout
    DataError.Network.UNAUTHORIZED -> R.string.feature_common_presentation_error_unauthorized
    DataError.Network.FORBIDDEN -> R.string.feature_common_presentation_error_forbidden
    DataError.Network.NOT_FOUND -> R.string.feature_common_presentation_error_not_found
    else -> R.string.feature_common_presentation_error_generic
}

private fun WorkoutValidationError.toStringResId(): Int = when (this) {
    is WorkoutValidationError.Name -> this.toStringResId()
    is WorkoutValidationError.Description -> this.toStringResId()
    is WorkoutValidationError.Value -> this.toStringResId()
    is WorkoutValidationError.Sets -> this.toStringResId()
    is WorkoutValidationError.MuscleGroups -> this.toStringResId()
    is WorkoutValidationError.WorkoutImage -> this.toStringResId()
    is WorkoutValidationError.Workouts -> this.toStringResId()
}


private fun DataError.Auth.toStringResId(): Int = when (this) {
    DataError.Auth.INVALID_CREDENTIAL -> R.string.feature_common_presentation_error_invalid_credential
    DataError.Auth.INVALID_USER -> R.string.feature_common_presentation_error_invalid_user
    DataError.Auth.UNAUTHENTICATED -> R.string.feature_common_presentation_error_unauthenticated
    else -> R.string.feature_common_presentation_error_generic
}

private fun DataError.Firestore.toStringResId(): Int = when (this) {
    DataError.Firestore.DocumentNotFound -> R.string.feature_common_presentation_error_document_not_found
    DataError.Firestore.PermissionDenied -> R.string.feature_common_presentation_error_permission_denied
    DataError.Firestore.Unauthenticated -> R.string.feature_common_presentation_error_unauthenticated
    else -> R.string.feature_common_presentation_error_generic
}

private fun ValidationError.Username.toStringResId(): Int = when (this) {
    ValidationError.Username.EMPTY -> R.string.feature_common_presentation_error_username_empty
    ValidationError.Username.TOO_SHORT -> R.string.feature_common_presentation_error_username_too_short
}

private fun ValidationError.Age.toStringResId(): Int = when (this) {
    ValidationError.Age.INVALID -> R.string.feature_common_presentation_error_age_invalid
    ValidationError.Age.TOO_YOUNG -> R.string.feature_common_presentation_error_age_too_young
}

private fun ValidationError.Gender.toStringResId(): Int = when (this) {
    ValidationError.Gender.EMPTY -> R.string.feature_common_presentation_error_gender_empty
    ValidationError.Gender.INVALID -> R.string.feature_common_presentation_error_gender_invalid
}

private fun ValidationError.Height.toStringResId(): Int = when (this) {
    ValidationError.Height.INVALID -> R.string.feature_common_presentation_error_height_invalid
    ValidationError.Height.TOO_LOW -> R.string.feature_common_presentation_error_height_too_low
}

private fun ValidationError.Weight.toStringResId(): Int = when (this) {
    ValidationError.Weight.INVALID -> R.string.feature_common_presentation_error_weight_invalid
    ValidationError.Weight.TOO_LOW -> R.string.feature_common_presentation_error_weight_too_low
}

private fun WorkoutValidationError.Name.toStringResId(): Int = when (this) {
    WorkoutValidationError.Name.EMPTY -> R.string.feature_common_presentation_error_name_empty
    WorkoutValidationError.Name.TOO_SHORT -> R.string.feature_common_presentation_error_name_too_short
}

private fun WorkoutValidationError.Description.toStringResId(): Int = when (this) {
    WorkoutValidationError.Description.EMPTY -> R.string.feature_common_presentation_error_description_empty
    WorkoutValidationError.Description.TOO_SHORT -> R.string.feature_common_presentation_error_description_too_short
}

private fun WorkoutValidationError.Value.toStringResId(): Int = when (this) {
    WorkoutValidationError.Value.INVALID -> R.string.feature_common_presentation_error_value_invalid
    WorkoutValidationError.Value.TOO_LOW -> R.string.feature_common_presentation_error_value_too_low
}

private fun WorkoutValidationError.Sets.toStringResId(): Int = when (this) {
    WorkoutValidationError.Sets.INVALID -> R.string.feature_common_presentation_error_sets_invalid
    WorkoutValidationError.Sets.TOO_MANY -> R.string.feature_common_presentation_error_sets_too_many
}

private fun WorkoutValidationError.MuscleGroups.toStringResId(): Int = when (this) {
    WorkoutValidationError.MuscleGroups.EMPTY -> R.string.feature_common_presentation_error_muscle_groups_empty
}

private fun WorkoutValidationError.WorkoutImage.toStringResId(): Int = when (this) {
    WorkoutValidationError.WorkoutImage.EMPTY -> R.string.feature_common_presentation_error_workout_image_empty
}

private fun WorkoutValidationError.Workouts.toStringResId(): Int = when (this) {
    WorkoutValidationError.Workouts.TOO_FEW -> R.string.feature_common_presentation_error_workouts_list_too_few
    WorkoutValidationError.Workouts.TOO_MANY -> R.string.feature_common_presentation_error_workouts_list_too_many
}


