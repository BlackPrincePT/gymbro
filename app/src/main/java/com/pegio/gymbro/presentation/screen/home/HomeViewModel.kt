package com.pegio.gymbro.presentation.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.usecase.common.FetchCurrentUserStreamUseCase
import com.pegio.gymbro.domain.usecase.drawer.SignOutUseCase
import com.pegio.gymbro.domain.usecase.feed.ObserveRelevantPostsStreamUseCase
import com.pegio.gymbro.presentation.model.mapper.UiPostMapper
import com.pegio.gymbro.presentation.model.mapper.UiUserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchCurrentUserStream: FetchCurrentUserStreamUseCase,
    private val observeRelevantPostsStream: ObserveRelevantPostsStreamUseCase,
    private val signOut: SignOutUseCase,
    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper
) : ViewModel() {

    var uiState by mutableStateOf(value = HomeUiState())
        private set

    private val _uiEffect = MutableSharedFlow<HomeUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        observeCurrentUser()
//        observePosts()
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
            .onSuccess { updateState { copy(currentUser = uiUserMapper.mapFromDomain(it)) } }
            .launchIn(viewModelScope)
    }

    private fun observePosts() {
        observeRelevantPostsStream()
            .onSuccess { updateState { copy(relevantPosts = it.map(uiPostMapper::mapFromDomain)) } }
    }

    private fun updateState(change: HomeUiState.() -> HomeUiState) {
        uiState = uiState.change()
    }

    private fun sendEffect(effect: HomeUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}