package com.pegio.presentation.screen.createpost

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.model.Post
import com.pegio.common.core.onSuccess
import com.pegio.domain.usecase.GetCurrentUserIdUseCase
import com.pegio.domain.usecase.UploadPostUseCase
import com.pegio.uploadmanager.core.FileUploadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val getCurrentUserId: GetCurrentUserIdUseCase,
    private val uploadPost: UploadPostUseCase,
    private val fileUploadManager: FileUploadManager,
) : ViewModel() {

    var uiState by mutableStateOf(value = CreatePostUiState())
        private set

    private val _uiEffect = MutableSharedFlow<CreatePostUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: CreatePostUiEvent) {
        when (event) {
            is CreatePostUiEvent.OnPostTextChange -> updateState { copy(postText = event.value) }
            is CreatePostUiEvent.OnPhotoSelected -> updateState { copy(imageUri = event.imageUri) }
            CreatePostUiEvent.OnCancelClick -> sendEffect(CreatePostUiEffect.NavigateBack)
            CreatePostUiEvent.OnPostClick -> handlePostClick()
        }
    }

    private fun handlePostClick() = viewModelScope.launch {
        uiState.imageUri?.let { uploadImagePost(uri = it) }
            ?: run {
                uploadPost(post = createPost())
                sendEffect(CreatePostUiEffect.NavigateBack)
            }
    }

    private suspend fun uploadImagePost(uri: Uri) {
        fileUploadManager.enqueueFileUpload(uri.toString())
            .onSuccess {
                uploadPost(post = createPost(imageUrl = it))
                sendEffect(CreatePostUiEffect.NavigateBack)
            }
    }

    private fun createPost(imageUrl: String? = null): Post {
        return Post.EMPTY
            .copy(
                authorId = getCurrentUserId(),
                content = uiState.postText,
                imageUrl = imageUrl,
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