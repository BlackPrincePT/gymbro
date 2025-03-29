package com.pegio.gymbro.domain.usecase.validator

import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.ValidationError
import com.pegio.gymbro.domain.model.User.Gender
import javax.inject.Inject

class ValidateGenderUseCase @Inject constructor() {
    operator fun invoke(gender: Gender?): Resource<Unit, ValidationError.Gender> {
        if (gender == null)
            return Resource.Failure(error = ValidationError.Gender.EMPTY)

        return Resource.Success(Unit)
    }
}