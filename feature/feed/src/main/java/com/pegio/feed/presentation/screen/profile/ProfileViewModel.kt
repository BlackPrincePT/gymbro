package com.pegio.feed.presentation.screen.profile

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pegio.common.core.getOrElse
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.domain.usecase.common.FetchUserByIdUseCase
import com.pegio.domain.usecase.feed.FetchLatestUserPostsUseCase
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.feed.presentation.screen.profile.navigation.ProfileRoute
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEffect
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEvent
import com.pegio.feed.presentation.screen.profile.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchUserById: FetchUserByIdUseCase,
    private val fetchLatestUserPosts: FetchLatestUserPostsUseCase,
    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ProfileUiState, ProfileUiEffect, ProfileUiEvent>(initialState = ProfileUiState()) {

    private val userId = savedStateHandle.toRoute<ProfileRoute>().userId

    init {
        loadProfileInfo()
    }

    override fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.OnBackClick -> sendEffect(ProfileUiEffect.NavigateBack)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun loadProfileInfo() = launchWithLoading {
        fetchUserById(id = userId)
            .onFailure { return@launchWithLoading } // TODO HANDLE PROPERLY
            .onSuccess { user ->
                updateState { copy(displayedUser = uiUserMapper.mapFromDomain(user)) }

                fetchLatestUserPosts(authorId = user.id)
                    .getOrElse { return@launchWithLoading } // TODO HANDLE PROPERLY
                    .map(uiPostMapper::mapFromDomain)
                    .let { updateState { copy(userPosts = it) } }
            }
    }
}