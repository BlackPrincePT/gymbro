package com.pegio.domain.usecase.validator.user

import com.pegio.common.core.Resource
import com.pegio.common.core.ValidationError
import com.pegio.model.User.Gender
import javax.inject.Inject

class ValidateGenderUseCase @Inject constructor() {
    operator fun invoke(gender: Gender?): Resource<Unit, ValidationError.Gender> {
        if (gender == null)
            return Resource.Failure(error = ValidationError.Gender.EMPTY)

        return Resource.Success(Unit)
    }
}