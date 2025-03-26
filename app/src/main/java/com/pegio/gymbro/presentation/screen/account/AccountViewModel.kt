package com.pegio.gymbro.presentation.screen.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.manager.upload.FileType
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import com.pegio.gymbro.domain.usecase.common.FetchUserStreamByIdUseCase
import com.pegio.gymbro.domain.usecase.common.GetCurrentUserIdUseCase
import com.pegio.gymbro.domain.usecase.register.SaveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserId: GetCurrentUserIdUseCase,
    private val fetchUserStreamById: FetchUserStreamByIdUseCase,
    private val fileUploadManager: FileUploadManager,
    private val saveUser: SaveUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AccountUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        fetchCurrentUser()
    }

    fun onEvent(event: AccountUiEvent) {
        when (event) {
            is AccountUiEvent.OnPhotoSelected -> updateProfileImage(uri = event.imageUri)
            is AccountUiEvent.OnPhotoUpload -> {
                deleteExistingProfileImage()
                uploadProfileImageUrl(url = event.imageUrl)
            }
        }
    }

    private fun uploadProfileImageUrl(url: String) {
        uiState.value.user.copy(profile = url)
            .let { saveUser(it) }
    }

    private fun updateProfileImage(uri: Uri) {
        fileUploadManager.enqueueFileUpload(uri, fileType = FileType.JPG)
            .also { sendEffect(AccountUiEffect.ProfilePictureUploadStarted(workId = it)) }
    }

    private fun deleteExistingProfileImage() {
        val currentProfileImageUrl = uiState.value.user.profile ?: return
        fileUploadManager.deleteFile(currentProfileImageUrl)
    }

    private fun fetchCurrentUser() = viewModelScope.launch {
        fetchUserStreamById(getCurrentUserId())
            .collectLatest { result ->
                when (result) {
                    is Resource.Success -> _uiState.update { it.copy(user = result.data) }
                    is Resource.Failure -> {}
                }
            }
    }

    private fun sendEffect(effect: AccountUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}