package com.pegio.feed.presentation.screen.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onSuccess
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.domain.usecase.common.FetchCurrentUserStreamUseCase
import com.pegio.domain.usecase.post.ObserveRelevantPostsStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchCurrentUserStream: FetchCurrentUserStreamUseCase,
    private val observeRelevantPostsStream: ObserveRelevantPostsStreamUseCase,
    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper
) : ViewModel() {

    var uiState by mutableStateOf(value = HomeUiState())
        private set

    private val _uiEffect = MutableSharedFlow<HomeUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        observeCurrentUser()
        loadMorePosts()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnCreatePostClick -> sendEffect(HomeUiEffect.NavigateToCreatePost)
            HomeUiEvent.OnLoadMorePosts -> loadMorePosts()

            // Top Bar
            HomeUiEvent.OnDrawerClick -> sendEffect(HomeUiEffect.OpenDrawer)
            HomeUiEvent.OnChatClick -> sendEffect(HomeUiEffect.NavigateToChat)
        }
    }

    private fun observeCurrentUser() {
        fetchCurrentUserStream()
            .onSuccess { updateState { copy(currentUser = uiUserMapper.mapFromDomain(it)) } }
            .launchIn(viewModelScope)
    }

    private fun loadMorePosts() = viewModelScope.launch {
        observeRelevantPostsStream()
            .onSuccess { updateState { copy(relevantPosts = relevantPosts.plus(it.map(uiPostMapper::mapFromDomain))) } }
    }

    private fun updateState(change: HomeUiState.() -> HomeUiState) {
        uiState = uiState.change()
    }

    private fun sendEffect(effect: HomeUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}