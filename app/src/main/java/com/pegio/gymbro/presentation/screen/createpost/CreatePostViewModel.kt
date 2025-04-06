package com.pegio.gymbro.presentation.screen.createpost

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.model.Post
import com.pegio.gymbro.domain.usecase.createpost.UploadPostUseCase
import com.pegio.gymbro.domain.usecase.register.GetCurrentUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val getCurrentUserId: GetCurrentUserIdUseCase,
    private val uploadPost: UploadPostUseCase
) : ViewModel() {

    var uiState by mutableStateOf(value = CreatePostUiState())
        private set

    private val _uiEffect = MutableSharedFlow<CreatePostUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: CreatePostUiEvent) {
        when (event) {
            is CreatePostUiEvent.OnPostTextChange -> updateState { copy(postText = event.value) }
            CreatePostUiEvent.OnCancelClick -> sendEffect(CreatePostUiEffect.NavigateBack)
            CreatePostUiEvent.OnPostClick -> {
                handlePostClick()
                sendEffect(CreatePostUiEffect.NavigateBack)
            }
        }
    }

    private fun handlePostClick() {
        uploadPost(post = createPost())
    }

    private fun createPost(): Post {
        return Post.EMPTY
            .copy(
                authorId = getCurrentUserId(),
                content = uiState.postText,
                imageUrl = null,
                timestamp = System.currentTimeMillis()
            )
    }

    private fun updateState(change: CreatePostUiState.() -> CreatePostUiState) {
        uiState = uiState.change()
    }

    private fun sendEffect(effect: CreatePostUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}