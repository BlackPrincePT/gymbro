package com.pegio.gymbro.presentation.screen.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.ifFailure
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import com.pegio.gymbro.domain.usecase.aggregator.FormValidatorUseCases
import com.pegio.gymbro.domain.usecase.register.GetCurrentUserIdUseCase
import com.pegio.gymbro.domain.usecase.register.SaveUserUseCase
import com.pegio.gymbro.presentation.mapper.UiUserMapper
import com.pegio.gymbro.presentation.model.UiUser
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
    private val saveUser: SaveUserUseCase,
    private val uiUserMapper: UiUserMapper,
    private val fileUploadManager: FileUploadManager,
    private val formValidator: FormValidatorUseCases,
    getCurrentUserId: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<RegisterUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        getCurrentUserId()?.let { currentUserId ->
            updateUser { copy(id = currentUserId) }
        }
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
        fileUploadManager.enqueueFileUpload(uri.toString()).also { result ->
            when (result) {
                is Resource.Failure -> { }
                is Resource.Success -> {
                    saveUser(uiUserMapper.mapToDomain(uiState.value.user.copy(imgProfileUrl = result.data)))
                    sendEffect(RegisterUiEffect.NavigateToHome)
                }
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        val currentUser = uiState.value.user

        var isValid = true

        formValidator.validateUsername(currentUser.username)
            .ifFailure { updateError { copy(username = it) }; isValid = it == null }

        formValidator.validateAge(ageString = currentUser.age)
            .ifFailure { updateError { copy(age = it) }; isValid = it == null }

        formValidator.validateGender(gender = currentUser.gender)
            .ifFailure { updateError { copy(gender = it) }; isValid = it == null }

        formValidator.validateHeight(heightString = currentUser.heightCm)
            .ifFailure { updateError { copy(height = it) }; isValid = it == null }

        formValidator.validateWeight(weightString = currentUser.weightKg)
            .ifFailure { updateError { copy(weight = it) }; isValid = it == null }

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