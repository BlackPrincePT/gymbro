package com.pegio.feed.presentation.screen.followrecord

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pegio.common.core.DataError
import com.pegio.common.core.getOrElse
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.domain.usecase.feed.FetchNextFollowersRecordPageUseCase
import com.pegio.domain.usecase.feed.FetchNextFollowingRecordPageUseCase
import com.pegio.feed.presentation.screen.followrecord.navigation.FollowRecordRoute
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiEffect
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiEvent
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiState
import com.pegio.model.FollowRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowRecordViewModel @Inject constructor(
    private val uiUserMapper: UiUserMapper,
    fetchNextFollowingRecordPage: FetchNextFollowingRecordPageUseCase,
    fetchNextFollowersRecordPage: FetchNextFollowersRecordPageUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<FollowRecordUiState, FollowRecordUiEffect, FollowRecordUiEvent>(initialState = FollowRecordUiState()) {


    private val args = savedStateHandle.toRoute<FollowRecordRoute>()

    val currentMode = args.mode
    private val userId = args.userId

    private val fnLoadMoreUsers = when (currentMode) {
        FollowRecord.Type.FOLLOWING -> fetchNextFollowingRecordPage::invoke
        FollowRecord.Type.FOLLOWERS -> fetchNextFollowersRecordPage::invoke
    }

    init {
        loadMoreUsers()
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onEvent(event: FollowRecordUiEvent) {
        when (event) {

            // Main
            FollowRecordUiEvent.OnLoadMoreUsers -> loadMoreUsers()

            // Navigation
            FollowRecordUiEvent.OnBackClick -> sendEffect(FollowRecordUiEffect.NavigateBack)
            is FollowRecordUiEvent.OnUserProfileClick ->
                sendEffect(FollowRecordUiEffect.NavigateToUserProfile(event.userId))
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun loadMoreUsers() {
        if (uiState.endOfUsersReached) return

        launchWithLoading {
            fnLoadMoreUsers.invoke(userId)
                .getOrElse { error ->
                    when (error) {
                        DataError.Pagination.END_OF_PAGINATION_REACHED ->
                            updateState { copy(endOfUsersReached = true) }

                        else -> {}
                    }
                    return@launchWithLoading
                }
                .map(uiUserMapper::mapFromDomain)
                .let { updateState { copy(users = it) } }
        }
    }
}