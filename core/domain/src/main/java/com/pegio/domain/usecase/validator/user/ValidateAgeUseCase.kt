package com.pegio.domain.usecase.validator.user

import com.pegio.common.core.Resource
import com.pegio.common.core.ValidationError
import javax.inject.Inject

class ValidateAgeUseCase @Inject constructor() {
    operator fun invoke(ageString: String): Resource<Unit, ValidationError.Age> {
        try {
            val age = ageString.toInt()

            if (age <= 0)
                return Resource.Failure(error = ValidationError.Age.INVALID)

            if (age < 18)
                return Resource.Failure(error = ValidationError.Age.TOO_YOUNG)

            return Resource.Success(Unit)

        } catch (_: NumberFormatException) {
            return Resource.Failure(error = ValidationError.Age.INVALID)
        }
    }
}