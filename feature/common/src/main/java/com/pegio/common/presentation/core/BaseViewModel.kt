package com.pegio.common.presentation.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class BaseViewModel<State, Effect, Event>(initialState: State) : ViewModel() {

    var uiState by mutableStateOf<State>(initialState)
        private set

    private val _uiEffect = Channel<Effect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    abstract fun onEvent(event: Event)

    protected fun updateState(transform: State.() -> State) {
        uiState = uiState.transform()
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.send(effect)
        }
    }

    protected open fun setLoading(isLoading: Boolean = false) { }

    protected fun launchWithLoading(
        updateLoading: (Boolean) -> Unit = ::setLoading,
        block: suspend () -> Unit
    ) {
        updateLoading(true)

        viewModelScope.launch {
            try { block() }
            finally { withContext(Dispatchers.Main.immediate) { updateLoading(false) } }
        }
    }
}