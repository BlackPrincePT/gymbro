package com.pegio.domain.usecase.aggregator

import com.pegio.domain.usecase.validator.user.ValidateAgeUseCase
import com.pegio.domain.usecase.validator.user.ValidateGenderUseCase
import com.pegio.domain.usecase.validator.user.ValidateHeightUseCase
import com.pegio.domain.usecase.validator.user.ValidateUsernameUseCase
import com.pegio.domain.usecase.validator.user.ValidateWeightUseCase
import javax.inject.Inject

data class FormValidatorUseCases @Inject constructor(
    val validateUsername: ValidateUsernameUseCase,
    val validateAge: ValidateAgeUseCase,
    val validateGender: ValidateGenderUseCase,
    val validateHeight: ValidateHeightUseCase,
    val validateWeight: ValidateWeightUseCase
)