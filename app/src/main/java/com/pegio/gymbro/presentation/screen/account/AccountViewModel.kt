package com.pegio.gymbro.presentation.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.usecase.common.FetchUserStreamByIdUseCase
import com.pegio.gymbro.domain.usecase.common.GetCurrentUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserId: GetCurrentUserIdUseCase,
    private val fetchUserStreamById: FetchUserStreamByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AccountUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        fetchCurrentUser()
    }

    fun onEvent(event: AccountUiEvent) {
        when (event) {
            AccountUiEvent.OnProfileImageClicked -> {}
        }
    }

    private fun fetchCurrentUser() = viewModelScope.launch {
        fetchUserStreamById(getCurrentUserId())
            .collectLatest { result ->
                when (result) {
                    is Resource.Success -> _uiState.update { it.copy(user = result.data) }
                    is Resource.Failure -> { }
                }
            }
    }
}