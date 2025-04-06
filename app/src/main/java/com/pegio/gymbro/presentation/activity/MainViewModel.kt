package com.pegio.gymbro.presentation.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var topBarState by mutableStateOf(TopBarState())
        private set

    fun updateTopBarState(state: TopBarState) {
        topBarState = state
    }
}