package com.pegio.gymbro.domain.usecase.validator

import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.ValidationError
import javax.inject.Inject

class ValidateHeightUseCase @Inject constructor() {
    operator fun invoke(heightString: String): Resource<Unit, ValidationError.Height> {
        try {
            val height = heightString.toInt()

            if (height <= 0)
                return Resource.Failure(error = ValidationError.Height.INVALID)

            return Resource.Success(Unit)

        } catch (_: NumberFormatException) {
            return Resource.Failure(error = ValidationError.Height.INVALID)
        }
    }
}