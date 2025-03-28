package com.pegio.gymbro.presentation.screen.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import com.pegio.gymbro.domain.usecase.common.FetchCurrentUserStreamUseCase
import com.pegio.gymbro.domain.usecase.register.SaveUserUseCase
import com.pegio.gymbro.presentation.mapper.UiUserMapper
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
    private val fetchCurrentUserStream: FetchCurrentUserStreamUseCase,
    private val fileUploadManager: FileUploadManager,
    private val saveUser: SaveUserUseCase,
    private val uiUserMapper: UiUserMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()

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
        }
    }

    private fun updateProfileImage(uri: Uri) = viewModelScope.launch {
        fileUploadManager.enqueueFileUpload(uri.toString()).also { result ->
            when (result) {
                is Resource.Success -> uploadProfileImageUrl(url = result.data)
                is Resource.Failure -> { }
            }
        }
    }

    private fun uploadProfileImageUrl(url: String) {
        uiState.value.user.copy(imgProfileUrl = url)
            .let { saveUser(uiUserMapper.mapToDomain(it)) }
    }

    private fun deleteExistingProfileImage() {
        val currentProfileImageUrl = uiState.value.user.imgProfileUrl ?: return
        fileUploadManager.deleteFile(currentProfileImageUrl)
    }

    private fun observeCurrentUser() = viewModelScope.launch {
        fetchCurrentUserStream()
            .collectLatest { result ->
                when (result) {
                    is Resource.Success -> _uiState.update { it.copy(user = uiUserMapper.mapFromDomain(result.data)) }
                    is Resource.Failure -> { }
                }
            }
    }

    private fun sendEffect(effect: AccountUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}