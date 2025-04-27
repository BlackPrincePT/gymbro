package com.pegio.auth.presentation.screen.register

import com.pegio.auth.presentation.screen.register.state.RegisterFormValue
import com.pegio.auth.presentation.screen.register.state.RegisterUiEffect
import com.pegio.auth.presentation.screen.register.state.RegisterUiEvent
import com.pegio.auth.presentation.screen.register.state.RegisterUiState
import com.pegio.auth.presentation.screen.register.state.RegisterValidationError
import com.pegio.common.core.errorOrNull
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.aggregator.FormValidatorUseCases
import com.pegio.domain.usecase.auth.RegisterCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerCurrentUser: RegisterCurrentUserUseCase,
    private val formValidator: FormValidatorUseCases,
) : BaseViewModel<RegisterUiState, RegisterUiEffect, RegisterUiEvent>(initialState = RegisterUiState()) {


    override fun onEvent(event: RegisterUiEvent) {
        when (event) {

            // Main
            RegisterUiEvent.OnSubmit -> handleSubmitClick()
            RegisterUiEvent.OnLaunchGallery -> handleGalleryLaunch()

            // Bottom Sheet
            is RegisterUiEvent.OnBottomSheetStateUpdate -> updateState { copy(shouldShowBottomSheet = event.shouldShow) }

            // Compose State
            is RegisterUiEvent.OnUsernameChanged -> updateForm { copy(username = event.username) }
            is RegisterUiEvent.OnAgeChanged -> updateForm { copy(age = event.age) }
            is RegisterUiEvent.OnGenderChanged -> updateForm { copy(gender = event.gender) }
            is RegisterUiEvent.OnHeightChanged -> updateForm { copy(height = event.height) }
            is RegisterUiEvent.OnWeightChanged -> updateForm { copy(weight = event.weight) }
            is RegisterUiEvent.OnProfilePhotoSelected -> updateState { copy(selectedImageUri = event.imageUri) }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun handleSubmitClick() = with(uiState.formValue) {
        if (!areFieldsValid()) return

        val ageValue = age.toIntOrNull()
        val heightValue = height.toIntOrNull()
        val weightValue = weight.toIntOrNull()

        if (ageValue == null || heightValue == null || weightValue == null || gender == null)
            return

        launchWithLoading {
            registerCurrentUser(
                username = username,
                age = ageValue,
                gender = gender,
                height = heightValue,
                weight = weightValue,
                imageUri = uiState.selectedImageUri?.toString()
            )
                .onSuccess { sendEffect(RegisterUiEffect.NavigateToHome) }
        }
    }

    private fun handleGalleryLaunch() {
        if (uiState.selectedImageUri != null) updateState { copy(shouldShowBottomSheet = true) }
        else sendEffect(RegisterUiEffect.LaunchGallery)
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun areFieldsValid(): Boolean = with(uiState.formValue) {

        val usernameError = formValidator.validateUsername(username).errorOrNull()
        val ageError = formValidator.validateAge(age).errorOrNull()
        val genderError = formValidator.validateGender(gender).errorOrNull()
        val heightError = formValidator.validateHeight(height).errorOrNull()
        val weightError = formValidator.validateWeight(weight).errorOrNull()

        updateError {
            copy(
                username = usernameError?.toStringResId(),
                age = ageError?.toStringResId(),
                gender = genderError?.toStringResId(),
                height = heightError?.toStringResId(),
                weight = weightError?.toStringResId()
            )
        }

        return usernameError == null && ageError == null && genderError == null && heightError == null && weightError == null
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\

    private fun updateForm(transform: RegisterFormValue.() -> RegisterFormValue) {
        updateState { copy(formValue = formValue.transform()) }
    }

    private fun updateError(transform: RegisterValidationError.() -> RegisterValidationError) {
        updateState { copy(validationError = validationError.transform()) }
    }
}