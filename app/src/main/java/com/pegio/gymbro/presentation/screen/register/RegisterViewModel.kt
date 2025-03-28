package com.pegio.gymbro.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.usecase.common.GetCurrentUserIdUseCase
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
    private val getCurrentUserId: GetCurrentUserIdUseCase,
    private val uiUserMapper: UiUserMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<RegisterUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.OnUsernameChanged -> _uiState.update { it.copy(username = event.newValue) }
            RegisterUiEvent.OnRegister -> {
                saveUser(uiUserMapper.mapToDomain(createUser()))
                sendEffect(RegisterUiEffect.NavigateToHome)
            }
        }
    }

    private fun createUser(): UiUser {
        return UiUser(
            id = getCurrentUserId(),
            username = _uiState.value.username,
            imgProfileUrl = null,
            imgBackgroundUrl = null
        )
    }

    private fun sendEffect(effect: RegisterUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}