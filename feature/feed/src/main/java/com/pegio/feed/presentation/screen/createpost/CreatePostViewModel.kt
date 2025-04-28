package com.pegio.feed.presentation.screen.createpost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pegio.common.core.Displayable
import com.pegio.common.core.Error
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.feed.UploadPostUseCase
import com.pegio.domain.usecase.workout.FetchWorkoutByIdUseCase
import com.pegio.feed.presentation.screen.createpost.navigation.CreatePostRoute
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiEffect
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiEvent
import com.pegio.feed.presentation.screen.createpost.state.CreatePostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val uploadPost: UploadPostUseCase,
    private val fetchWorkoutById: FetchWorkoutByIdUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CreatePostUiState, CreatePostUiEffect, CreatePostUiEvent>(initialState = CreatePostUiState()) {


    private val args = savedStateHandle.toRoute<CreatePostRoute>()

    private val shouldOpenGalleryOnLaunch = args.shouldOpenGallery
    private var selectedWorkoutId: String? = null

    init {
        if (shouldOpenGalleryOnLaunch) sendEffect(CreatePostUiEffect.LaunchGallery)
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onEvent(event: CreatePostUiEvent) {
        when (event) {

            // Main
            CreatePostUiEvent.OnPostClick -> handlePostClick()
            CreatePostUiEvent.OnOpenGallery -> sendEffect(CreatePostUiEffect.LaunchGallery)
            is CreatePostUiEvent.OnSelectedWorkoutUpdate -> loadWorkoutInfo(event.id)

            // Navigation
            CreatePostUiEvent.OnCancelClick -> sendEffect(CreatePostUiEffect.NavigateBack)
            CreatePostUiEvent.OnChooseWorkoutClick -> sendEffect(CreatePostUiEffect.NavigateToChooseWorkout)

            // Compose State
            is CreatePostUiEvent.OnPhotoSelected -> updateState { copy(imageUri = event.imageUri) }
            is CreatePostUiEvent.OnPostTextChange -> updateState { copy(postText = event.value) }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun loadWorkoutInfo(id: String?) = id?.let { workoutId ->
        selectedWorkoutId = workoutId

        viewModelScope.launch {
            fetchWorkoutById(workoutId)
                .onSuccess { updateState { copy(selectedWorkoutTitle = it.title) } }
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun handlePostClick() = launchWithLoading {
        with(uiState) {
            uploadPost(content = postText, imageUri = imageUri?.toString(), workoutId = selectedWorkoutId)
                .onSuccess { sendEffect(CreatePostUiEffect.NavigateBack) }
                .onFailure { showDisplayableError(it) }
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun showDisplayableError(error: Error) {
        if (error is Displayable) sendEffect(CreatePostUiEffect.ShowSnackbar(error.toStringResId()))
    }
}