package com.pegio.gymbro.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.gymbro.activity.state.MainActivityUiEffect
import com.pegio.gymbro.activity.state.MainActivityUiEvent
import com.pegio.gymbro.activity.state.MainActivityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
//    private val signOut: SignOutUseCase,
//    observeCurrentUserStream: FetchCurrentUserStreamUseCase,
    uiUserMapper: UiUserMapper
) : ViewModel() {

    var uiState by mutableStateOf(MainActivityUiState())
        private set

    private val _uiEffect = MutableSharedFlow<MainActivityUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
//        observeCurrentUserStream()
//            .onSuccess { updateState { copy(currentUser = uiUserMapper.mapFromDomain(it)) } }
//            .launchIn(viewModelScope)
    }

    fun onEvent(event: MainActivityUiEvent) {
        when (event) {

            // App Bar
            is MainActivityUiEvent.OnUpdateTopBarState -> updateState { copy(topBarState = event.value) }

            // Open-Close Drawer
            MainActivityUiEvent.OnOpenDrawer -> sendEffect(MainActivityUiEffect.OpenDrawer)
            MainActivityUiEvent.OnCloseDrawer -> sendEffect(MainActivityUiEffect.CloseDrawer)

            // Drawer
            MainActivityUiEvent.OnAccountClick -> {
                sendEffect(MainActivityUiEffect.NavigateToAccount)
                sendEffect(MainActivityUiEffect.CloseDrawer)
            }
            MainActivityUiEvent.OnWorkoutPlanClick -> {
                sendEffect(MainActivityUiEffect.NavigateToWorkoutPlan)
                sendEffect(MainActivityUiEffect.CloseDrawer)
            }
            MainActivityUiEvent.OnSignOutClick -> {
//                signOut()
                sendEffect(MainActivityUiEffect.NavigateToAuth)
                sendEffect(MainActivityUiEffect.CloseDrawer)
            }
        }
    }

    private fun updateState(change: MainActivityUiState.() -> MainActivityUiState) {
        uiState = uiState.change()
    }

    private fun sendEffect(effect: MainActivityUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}