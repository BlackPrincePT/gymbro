package com.pegio.feed.presentation.screen.profile

import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEffect
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEvent
import com.pegio.feed.presentation.screen.profile.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : BaseViewModel<ProfileUiState, ProfileUiEffect, ProfileUiEvent>(initialState = ProfileUiState()) {

    override fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.OnBackClick -> TODO()
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}