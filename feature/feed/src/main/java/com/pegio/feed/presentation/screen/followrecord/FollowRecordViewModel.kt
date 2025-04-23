package com.pegio.feed.presentation.screen.followrecord

import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiEffect
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiEvent
import com.pegio.feed.presentation.screen.followrecord.state.FollowRecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowRecordViewModel @Inject constructor(

) : BaseViewModel<FollowRecordUiState, FollowRecordUiEffect, FollowRecordUiEvent>(initialState = FollowRecordUiState()) {

    override fun onEvent(event: FollowRecordUiEvent) {
        TODO("Not yet implemented")
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}