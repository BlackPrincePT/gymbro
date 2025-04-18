package com.pegio.feed.presentation.screen.postdetails

import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEffect
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEvent
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(

) : BaseViewModel<PostDetailsUiState, PostDetailsUiEffect, PostDetailsUiEvent>(initialState = PostDetailsUiState()) {

    init {
        //TODO: LOAD FIRST BATCH OF COMMENTS
    }

    override fun onEvent(event: PostDetailsUiEvent) {
        when (event) {

            // Navigation
            PostDetailsUiEvent.OnBackClick -> sendEffect(PostDetailsUiEffect.NavigateBack)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}