package com.pegio.feed.presentation.screen.createpost

import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.core.retryableCall
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.feed.UploadPostUseCase
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiEffect
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiEvent
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val uploadPost: UploadPostUseCase
) : BaseViewModel<CreatePostUiState, CreatePostUiEffect, CreatePostUiEvent>(initialState = CreatePostUiState()) {

    override fun onEvent(event: CreatePostUiEvent) {
        when (event) {

            // Top Bar
            CreatePostUiEvent.OnCancelClick -> sendEffect(CreatePostUiEffect.NavigateBack)
            CreatePostUiEvent.OnPostClick -> handlePostClick()

            // Main
            is CreatePostUiEvent.OnPhotoSelected -> updateState { copy(imageUri = event.imageUri) }

            // Compose State
            is CreatePostUiEvent.OnPostTextChange -> updateState { copy(postText = event.value) }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun handlePostClick() = launchWithLoading {
        with(uiState) {
            retryableCall { uploadPost(content = postText, imageUri = imageUri.toString()) }
                .onSuccess { sendEffect(CreatePostUiEffect.NavigateBack) }
                .onFailure { sendEffect(CreatePostUiEffect.ShowSnackbar(it.toStringResId())) }
        }
    }
}