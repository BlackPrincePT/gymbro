package com.pegio.feed.presentation.screen.feed

import androidx.lifecycle.viewModelScope
import com.pegio.common.core.getOrElse
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.domain.usecase.common.GetCurrentUserStreamUseCase
import com.pegio.domain.usecase.feed.FetchNextRelevantPostsPageUseCase
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.feed.presentation.screen.feed.state.FeedUiEffect
import com.pegio.feed.presentation.screen.feed.state.FeedUiEvent
import com.pegio.feed.presentation.screen.feed.state.FeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val fetchCurrentUserStream: GetCurrentUserStreamUseCase,
    private val fetchNextRelevantPostsPage: FetchNextRelevantPostsPageUseCase,
    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper
) : BaseViewModel<FeedUiState, FeedUiEffect, FeedUiEvent>(initialState = FeedUiState()) {

    init {
        observeCurrentUser()
        loadMorePosts()
    }

    override fun onEvent(event: FeedUiEvent) {
        when (event) {
            FeedUiEvent.OnLoadMorePosts -> loadMorePosts()

            // Top Bar
            FeedUiEvent.OnDrawerClick -> sendEffect(FeedUiEffect.OpenDrawer)
            FeedUiEvent.OnChatClick -> sendEffect(FeedUiEffect.NavigateToChat)

            // Navigation
            FeedUiEvent.OnCreatePostClick -> sendEffect(FeedUiEffect.NavigateToCreatePost)
            is FeedUiEvent.OnPostCommentClick -> sendEffect(FeedUiEffect.NavigateToPostDetails(event.postId))
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun observeCurrentUser() = viewModelScope.launch {
        fetchCurrentUserStream()
            .getOrElse { return@launch }
            .collectLatest { updateState { copy(currentUser = uiUserMapper.mapFromDomain(it)) } }
    }

    private fun loadMorePosts() = launchWithLoading {
        fetchNextRelevantPostsPage()
            .onSuccess {
                val fetchedPosts = it.map(uiPostMapper::mapFromDomain)
                updateState { copy(relevantPosts = relevantPosts.plus(fetchedPosts)) }
            }
            .onFailure { /* TODO: HANDLE */ }
    }
}