package com.pegio.feed.presentation.screen.createpost

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pegio.common.core.Displayable
import com.pegio.common.core.Error
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.feed.UploadPostUseCase
import com.pegio.feed.presentation.screen.createpost.navigation.CreatePostRoute
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiEffect
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiEvent
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val uploadPost: UploadPostUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CreatePostUiState, CreatePostUiEffect, CreatePostUiEvent>(initialState = CreatePostUiState()) {

    companion object {
        const val SELECTED_WORKOUT_KEY = "selected_workout"
    }

    private val args = savedStateHandle.toRoute<CreatePostRoute>()

    private val shouldOpenGalleryOnLaunch = args.shouldOpenGallery
    private val selectedWorkoutId = savedStateHandle.get<String>(SELECTED_WORKOUT_KEY)

    init {
        if (shouldOpenGalleryOnLaunch) sendEffect(CreatePostUiEffect.LaunchGallery)
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onEvent(event: CreatePostUiEvent) {
        when (event) {

            // Main
            CreatePostUiEvent.OnPostClick -> handlePostClick()
            CreatePostUiEvent.OnOpenGallery -> sendEffect(CreatePostUiEffect.LaunchGallery)

            // Navigation
            CreatePostUiEvent.OnCancelClick -> sendEffect(CreatePostUiEffect.NavigateBack)

            // Compose State
            is CreatePostUiEvent.OnPhotoSelected -> updateState { copy(imageUri = event.imageUri) }
            is CreatePostUiEvent.OnPostTextChange -> updateState { copy(postText = event.value) }
            CreatePostUiEvent.OnChooseWorkoutClick -> { }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun handlePostClick() = launchWithLoading {
        with(uiState) {
            uploadPost(content = postText, imageUri = imageUri?.toString())
                .onSuccess { sendEffect(CreatePostUiEffect.NavigateBack) }
                .onFailure { showDisplayableError(it) }
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun showDisplayableError(error: Error) {
        if (error is Displayable) sendEffect(CreatePostUiEffect.ShowSnackbar(error.toStringResId()))
    }
}