package com.pegio.gymbro.presentation.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.usecase.drawer.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDrawerViewModel @Inject constructor(
    private val signOut: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppDrawerUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AppDrawerUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: AppDrawerUiEvent) {
        when (event) {
            AppDrawerUiEvent.OnAccountClicked -> sendEffect(AppDrawerUiEffect.NavigateToAccount)
            AppDrawerUiEvent.OnSignOut -> {
                signOut()
                sendEffect(AppDrawerUiEffect.NavigateToAuth)
            }
        }
    }

    private fun sendEffect(effect: AppDrawerUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}