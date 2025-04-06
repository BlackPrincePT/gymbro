package com.pegio.gymbro.presentation.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.model.User
import com.pegio.gymbro.domain.usecase.common.FetchCurrentUserStreamUseCase
import com.pegio.gymbro.presentation.model.UiUser
import com.pegio.gymbro.presentation.model.mapper.UiUserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeCurrentUserStream: FetchCurrentUserStreamUseCase,
    uiUserMapper: UiUserMapper
) : ViewModel() {

    var topBarState by mutableStateOf(TopBarState())
        private set

    var currentUser by mutableStateOf<UiUser?>(null)
        private set

    init {
        observeCurrentUserStream()
            .onSuccess { currentUser = uiUserMapper.mapFromDomain(it) }
            .launchIn(viewModelScope)
    }

    fun updateTopBarState(state: TopBarState) {
        topBarState = state
    }
}