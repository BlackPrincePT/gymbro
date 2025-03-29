package com.pegio.gymbro.domain.usecase.aggregator

import com.pegio.gymbro.domain.usecase.validator.ValidateAgeUseCase
import com.pegio.gymbro.domain.usecase.validator.ValidateGenderUseCase
import com.pegio.gymbro.domain.usecase.validator.ValidateHeightUseCase
import com.pegio.gymbro.domain.usecase.validator.ValidateUsernameUseCase
import com.pegio.gymbro.domain.usecase.validator.ValidateWeightUseCase
import javax.inject.Inject

data class FormValidatorUseCases @Inject constructor(
    val validateUsername: ValidateUsernameUseCase,
    val validateAge: ValidateAgeUseCase,
    val validateGender: ValidateGenderUseCase,
    val validateHeight: ValidateHeightUseCase,
    val validateWeight: ValidateWeightUseCase
)