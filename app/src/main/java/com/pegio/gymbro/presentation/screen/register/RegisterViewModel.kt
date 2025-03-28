package com.pegio.gymbro.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    getCurrentUserId: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<RegisterUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        getCurrentUserId()?.let { currentUserId ->
            _uiState.update { oldState ->
                oldState.copy(user = oldState.user.copy(id = currentUserId))
            }
        }
    }

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.OnUsernameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(user = currentState.user.copy(username = event.username))
                }
            }

            is RegisterUiEvent.OnAgeChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(user = currentState.user.copy(age = event.age))
                }
            }

            is RegisterUiEvent.OnGenderChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(user = currentState.user.copy(gender = event.gender))
                }
            }

            is RegisterUiEvent.OnHeightChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(user = currentState.user.copy(heightCm = event.height))
                }
            }

            is RegisterUiEvent.OnWeightChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(user = currentState.user.copy(weightKg = event.weight))
                }
            }

            RegisterUiEvent.OnProfilePhotoClicked -> {

            }

            RegisterUiEvent.OnSubmit -> {
                saveUser(uiUserMapper.mapToDomain(uiState.value.user))
                sendEffect(RegisterUiEffect.NavigateToHome)
            }
        }
    }

    private fun sendEffect(effect: RegisterUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}