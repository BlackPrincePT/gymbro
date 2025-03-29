package com.pegio.gymbro.domain.usecase.validator

import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.ValidationError
import javax.inject.Inject

class ValidateWeightUseCase @Inject constructor() {
    operator fun invoke(weightString: String): Resource<Unit, ValidationError.Weight> {
        try {
            val weight = weightString.toInt()

            if (weight <= 0)
                return Resource.Failure(error = ValidationError.Weight.INVALID)

            return Resource.Success(Unit)

        } catch (_: NumberFormatException) {
            return Resource.Failure(error = ValidationError.Weight.INVALID)
        }
    }
}