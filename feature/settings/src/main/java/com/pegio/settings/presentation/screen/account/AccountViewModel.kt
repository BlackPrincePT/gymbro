package com.pegio.settings.presentation.screen.account

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.errorOrNull
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.aggregator.FormValidatorUseCases
import com.pegio.domain.usecase.common.DeleteFileUseCase
import com.pegio.domain.usecase.common.EnqueueFileUploadUseCase
import com.pegio.domain.usecase.common.GetCurrentUserStreamUseCase
import com.pegio.domain.usecase.common.SaveUserUseCase
import com.pegio.settings.presentation.screen.account.state.AccountFormValue
import com.pegio.settings.presentation.screen.account.state.AccountUiEffect
import com.pegio.settings.presentation.screen.account.state.AccountUiEvent
import com.pegio.settings.presentation.screen.account.state.AccountUiState
import com.pegio.settings.presentation.screen.account.state.AccountValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserStream: GetCurrentUserStreamUseCase,
    private val saveUser: SaveUserUseCase,

    private val enqueueFileUpload: EnqueueFileUploadUseCase,
    private val deleteFile: DeleteFileUseCase,

    private val formValidator: FormValidatorUseCases,

    private val uiUserMapper: UiUserMapper
) : BaseViewModel<AccountUiState, AccountUiEffect, AccountUiEvent>(initialState = AccountUiState()) {


    init {
        observeCurrentUser()
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onEvent(event: AccountUiEvent) {
        when (event) {

            // Main
            AccountUiEvent.OnLaunchGallery -> sendEffect(AccountUiEffect.LaunchGallery)
            is AccountUiEvent.OnPhotoSelected -> updateProfileImage(uri = event.imageUri)

            // Form
            AccountUiEvent.OnUsernameSubmit,
            AccountUiEvent.OnAgeSubmit,
            AccountUiEvent.OnGenderSubmit,
            AccountUiEvent.OnHeightSubmit,
            AccountUiEvent.OnWeightSubmit -> validateAndUpdateUserInfo(event)

            // Navigation
            AccountUiEvent.OnBackClick -> sendEffect(AccountUiEffect.NavigateBack)

            // Compose State
            is AccountUiEvent.OnUsernameChanged -> updateFormState { copy(username = event.username) }
            is AccountUiEvent.OnAgeChanged -> updateFormState { copy(age = event.age) }
            is AccountUiEvent.OnGenderChanged -> updateFormState { copy(gender = event.gender) }
            is AccountUiEvent.OnHeightChanged -> updateFormState { copy(height = event.height) }
            is AccountUiEvent.OnWeightChanged -> updateFormState { copy(weight = event.weight) }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoadingAvatar = isLoading) }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun updateProfileImage(uri: Uri) = launchWithLoading {
        with(uiState.user) {
            enqueueFileUpload(uri.toString())
                .onSuccess { url ->
                    avatarUrl?.let { deleteFile(it) }
                    saveUser(uiUserMapper.mapToDomain(copy(avatarUrl = url)))
                }
        }
    }

    private fun validateAndUpdateUserInfo(event: AccountUiEvent) {
        when (event) {
            AccountUiEvent.OnUsernameSubmit -> with(uiState) {
                val usernameError = formValidator.validateUsername(formValue.username).errorOrNull()
                updateErrorState { copy(username = usernameError?.toStringResId()) }

                if (usernameError == null) {
                    updateUserInfo { copy(username = formValue.username) }
                    updateFormState { copy(username = "") }
                }
            }

            AccountUiEvent.OnAgeSubmit -> with(uiState) {
                val ageError = formValidator.validateAge(formValue.age).errorOrNull()
                updateErrorState { copy(age = ageError?.toStringResId()) }

                if (ageError == null) {
                    updateUserInfo { copy(age = formValue.age) }
                    updateFormState { copy(age = "") }
                }
            }

            AccountUiEvent.OnGenderSubmit -> with(uiState) {
                val genderError = formValidator.validateGender(formValue.gender).errorOrNull()
                updateErrorState { copy(gender = genderError?.toStringResId()) }

                if (genderError == null) {
                    updateUserInfo { copy(gender = formValue.gender!!) }
                    updateFormState { copy(gender = null) }
                }
            }

            AccountUiEvent.OnHeightSubmit -> with(uiState) {
                val heightError = formValidator.validateHeight(formValue.height).errorOrNull()
                updateErrorState { copy(height = heightError?.toStringResId()) }

                if (heightError == null) {
                    updateUserInfo { copy(heightCm = formValue.height) }
                    updateFormState { copy(height = "") }
                }
            }

            AccountUiEvent.OnWeightSubmit -> with(uiState) {
                val weightError = formValidator.validateWeight(formValue.weight).errorOrNull()
                updateErrorState { copy(weight = weightError?.toStringResId()) }

                if (weightError == null) {
                    updateUserInfo { copy(weightKg = formValue.weight) }
                    updateFormState { copy(weight = "") }
                }
            }

            else -> return
        }
    }

    private fun updateUserInfo(transform: UiUser.() -> UiUser) {
        val updatedUser = uiState.user.transform()
        saveUser(uiUserMapper.mapToDomain(updatedUser))
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun observeCurrentUser() = viewModelScope.launch {
        getCurrentUserStream()
            .onSuccess { updateState { copy(user = uiUserMapper.mapFromDomain(it)) } }
            .onFailure {  } // TODO HANDLE FAILURE
            .launchIn(viewModelScope)
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun updateFormState(transform: AccountFormValue.() -> AccountFormValue) {
        updateState { copy(formValue = formValue.transform()) }
    }

    private fun updateErrorState(transform: AccountValidationError.() -> AccountValidationError) {
        updateState { copy(validationError = validationError.transform()) }
    }
}