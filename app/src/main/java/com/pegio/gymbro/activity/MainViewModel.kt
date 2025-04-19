package com.pegio.gymbro.activity

import androidx.lifecycle.viewModelScope
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.domain.usecase.account.SignOutUseCase
import com.pegio.domain.usecase.common.GetCurrentUserStreamUseCase
import com.pegio.gymbro.activity.state.MainActivityUiEffect
import com.pegio.gymbro.activity.state.MainActivityUiEvent
import com.pegio.gymbro.activity.state.MainActivityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val signOut: SignOutUseCase,
    private val getCurrentUserStream: GetCurrentUserStreamUseCase,
    private val uiUserMapper: UiUserMapper
) : BaseViewModel<MainActivityUiState, MainActivityUiEffect, MainActivityUiEvent>(initialState = MainActivityUiState()) {

    init {
        observeCurrentUser()
    }

    override fun onEvent(event: MainActivityUiEvent) {
        when (event) {

            // Top Bar
            is MainActivityUiEvent.OnUpdateTopBarState -> updateState { copy(topBarState = event.value) }

            // Drawer
            MainActivityUiEvent.OnOpenDrawer -> sendEffect(MainActivityUiEffect.OpenDrawer)
            MainActivityUiEvent.OnAccountClick -> sendDrawerEffect(MainActivityUiEffect.NavigateToAccount)
            MainActivityUiEvent.OnWorkoutPlanClick -> sendDrawerEffect(MainActivityUiEffect.NavigateToWorkoutPlan)
            MainActivityUiEvent.OnSignOutClick -> handleSignOut()
        }
    }

    override fun setLoading(isLoading: Boolean) {}

    private fun observeCurrentUser() = viewModelScope.launch {
        getCurrentUserStream()
            .collectLatest { updateState { copy(currentUser = uiUserMapper.mapFromDomain(it)) } }
    }

    private fun handleSignOut() {
        signOut()
        sendDrawerEffect(MainActivityUiEffect.NavigateToAuth)
    }

    private fun sendDrawerEffect(drawerEffect: MainActivityUiEffect) {
        sendEffect(drawerEffect)
        sendEffect(MainActivityUiEffect.CloseDrawer)
    }
}