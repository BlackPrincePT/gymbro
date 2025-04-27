package com.pegio.domain.usecase.validator.user

import com.pegio.common.core.Resource
import com.pegio.common.core.ValidationError
import javax.inject.Inject

class ValidateUsernameUseCase @Inject constructor() {
    operator fun invoke(username: String): Resource<Unit, ValidationError.Username> {
        if (username.isBlank())
            return Resource.Failure(error = ValidationError.Username.EMPTY)

        if (username.length < 3)
            return Resource.Failure(error = ValidationError.Username.TOO_SHORT)

        return Resource.Success(Unit)
    }
}