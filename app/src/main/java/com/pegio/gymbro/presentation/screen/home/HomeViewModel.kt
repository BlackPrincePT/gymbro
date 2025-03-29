package com.pegio.gymbro.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.usecase.common.FetchCurrentUserStreamUseCase
import com.pegio.gymbro.domain.usecase.drawer.SignOutUseCase
import com.pegio.gymbro.presentation.mapper.UiUserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchCurrentUserStream: FetchCurrentUserStreamUseCase,
    private val signOut: SignOutUseCase,
    private val uiUserMapper: UiUserMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<HomeUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        observeCurrentUser()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnAccountClick -> sendEffect(HomeUiEffect.NavigateToAccount)
            HomeUiEvent.OnSignOut -> {
                signOut()
                sendEffect(HomeUiEffect.SignedOutSuccessfully)
            }
        }
    }

    private fun observeCurrentUser() {
        fetchCurrentUserStream()
            .onSuccess { user -> _uiState.update { it.copy(displayedUser = uiUserMapper.mapFromDomain(user)) } }
            .launchIn(viewModelScope)
    }

    private fun sendEffect(effect: HomeUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}