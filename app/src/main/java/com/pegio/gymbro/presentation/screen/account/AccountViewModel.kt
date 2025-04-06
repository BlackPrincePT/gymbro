package com.pegio.gymbro.presentation.screen.account

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import com.pegio.gymbro.domain.usecase.common.FetchCurrentUserStreamUseCase
import com.pegio.gymbro.domain.usecase.register.SaveUserUseCase
import com.pegio.gymbro.presentation.model.mapper.UiUserMapper
import com.pegio.gymbro.presentation.screen.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val fetchCurrentUserStream: FetchCurrentUserStreamUseCase,
    private val fileUploadManager: FileUploadManager,
    private val saveUser: SaveUserUseCase,
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
        uiState.user.copy(imgProfileUrl = url)
            .let { saveUser(uiUserMapper.mapToDomain(it)) }
    }

    private fun deleteExistingProfileImage() {
        val currentProfileImageUrl = uiState.user.imgProfileUrl ?: return
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