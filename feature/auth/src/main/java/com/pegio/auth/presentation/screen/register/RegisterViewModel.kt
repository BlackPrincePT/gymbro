package com.pegio.auth.presentation.screen.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.errorOrNull
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.common.presentation.util.toStringResId
import com.pegio.uploadmanager.core.FileUploadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val saveUser: com.pegio.domain.usecase.common.SaveUserUseCase,
    private val uiUserMapper: UiUserMapper,
    private val fileUploadManager: FileUploadManager,
    private val formValidator: com.pegio.domain.usecase.aggregator.FormValidatorUseCases,
    getCurrentUserId: com.pegio.domain.usecase.common.GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<RegisterUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        updateUser { copy(id = getCurrentUserId()) }
    }

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.OnUsernameChanged -> updateUser { copy(username = event.username) }
            is RegisterUiEvent.OnAgeChanged -> updateUser { copy(age = event.age) }
            is RegisterUiEvent.OnGenderChanged -> updateUser { copy(gender = event.gender) }
            is RegisterUiEvent.OnHeightChanged -> updateUser { copy(heightCm = event.height) }
            is RegisterUiEvent.OnWeightChanged -> updateUser { copy(weightKg = event.weight) }
            is RegisterUiEvent.OnProfilePhotoSelected -> _uiState.update { it.copy(selectedImageUri = event.imageUri) }
            RegisterUiEvent.OnSubmit -> onSubmit()
        }
    }

    private fun onSubmit() {
        if (areFieldsValid().not())
            return

        uiState.value.selectedImageUri?.let { imageUri ->
            saveUserWithProfilePhoto(uri = imageUri)
        } ?: run {
            saveUser(uiUserMapper.mapToDomain(uiState.value.user))
            sendEffect(RegisterUiEffect.NavigateToHome)
        }
    }

    private fun saveUserWithProfilePhoto(uri: Uri) = viewModelScope.launch {
        fileUploadManager.enqueueFileUpload(uri.toString())
            .onSuccess {
                saveUser(uiUserMapper.mapToDomain(uiState.value.user.copy(avatarUrl = it)))
                sendEffect(RegisterUiEffect.NavigateToHome)
            }
    }

    private fun areFieldsValid(): Boolean {
        val currentUser = uiState.value.user

        var isValid = true

        formValidator.validateUsername(currentUser.username)
            .errorOrNull()
            .also { updateError { copy(username = it?.toStringResId()) }; isValid = it == null }

        formValidator.validateAge(ageString = currentUser.age)
            .errorOrNull()
            .also { updateError { copy(age = it?.toStringResId()) }; isValid = it == null }

        formValidator.validateGender(gender = currentUser.gender)
            .errorOrNull()
            .also { updateError { copy(gender = it?.toStringResId()) }; isValid = it == null }

        formValidator.validateHeight(heightString = currentUser.heightCm)
            .errorOrNull()
            .also { updateError { copy(height = it?.toStringResId()) }; isValid = it == null }

        formValidator.validateWeight(weightString = currentUser.weightKg)
            .errorOrNull()
            .also { updateError { copy(weight = it?.toStringResId()) }; isValid = it == null }

        return isValid
    }

    private fun updateUser(update: UiUser.() -> UiUser) {
        _uiState.update { currentState ->
            currentState.copy(user = currentState.user.update())
        }
    }

    private fun updateError(update: RegisterValidationError.() -> RegisterValidationError) {
        _uiState.update { currentState ->
            currentState.copy(validationError = currentState.validationError.update())
        }
    }

    private fun sendEffect(effect: RegisterUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}