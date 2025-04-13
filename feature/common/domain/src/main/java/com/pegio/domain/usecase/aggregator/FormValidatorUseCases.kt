package com.pegio.domain.usecase.aggregator

import com.pegio.domain.usecase.validator.ValidateAgeUseCase
import com.pegio.domain.usecase.validator.ValidateGenderUseCase
import com.pegio.domain.usecase.validator.ValidateHeightUseCase
import com.pegio.domain.usecase.validator.ValidateUsernameUseCase
import com.pegio.domain.usecase.validator.ValidateWeightUseCase
import javax.inject.Inject

data class FormValidatorUseCases @Inject constructor(
    val validateUsername: ValidateUsernameUseCase,
    val validateAge: ValidateAgeUseCase,
    val validateGender: ValidateGenderUseCase,
    val validateHeight: ValidateHeightUseCase,
    val validateWeight: ValidateWeightUseCase
)