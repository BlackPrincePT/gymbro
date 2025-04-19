package com.pegio.settings.presentation.screen

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.uploadmanager.core.FileUploadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val fetchCurrentUserStream: com.pegio.domain.usecase.common.FetchCurrentUserStreamUseCase,
    private val fileUploadManager: FileUploadManager,
    private val saveUser: com.pegio.domain.usecase.common.SaveUserUseCase,
    private val uiUserMapper: UiUserMapper
) : ViewModel() {

    var uiState by mutableStateOf(AccountUiState())
        private set

    private val _uiEffect = MutableSharedFlow<AccountUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        observeCurrentUser()
    }

    fun onEvent(event: AccountUiEvent) {
        when (event) {
            is AccountUiEvent.OnPhotoSelected -> updateProfileImage(uri = event.imageUri)
            is AccountUiEvent.OnPhotoUpload -> {
                deleteExistingProfileImage()
                uploadProfileImageUrl(url = event.imageUrl)
            }

            // Top Bar
            AccountUiEvent.OnBackClick -> sendEffect(AccountUiEffect.NavigateBack)
        }
    }

    private fun updateProfileImage(uri: Uri) = viewModelScope.launch {
        fileUploadManager.enqueueFileUpload(uri.toString())
            .onSuccess { uploadProfileImageUrl(url = it) }
    }

    private fun uploadProfileImageUrl(url: String) {
        uiState.user.copy(avatarUrl = url)
            .let { saveUser(uiUserMapper.mapToDomain(it)) }
    }

    private fun deleteExistingProfileImage() {
        val currentProfileImageUrl = uiState.user.avatarUrl ?: return
        fileUploadManager.deleteFile(currentProfileImageUrl)
    }

    private fun observeCurrentUser() {
        fetchCurrentUserStream()
            .onSuccess { user -> updateState { copy(user = uiUserMapper.mapFromDomain(user)) } }
            .launchIn(viewModelScope)
    }

    private fun updateState(change: AccountUiState.() -> AccountUiState) {
        uiState = uiState.change()
    }

    private fun sendEffect(effect: AccountUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}