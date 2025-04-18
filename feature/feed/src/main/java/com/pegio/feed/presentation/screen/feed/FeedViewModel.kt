package com.pegio.feed.presentation.screen.feed

import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.domain.usecase.common.FetchCurrentUserStreamUseCase
import com.pegio.domain.usecase.post.FetchRelevantPostsUseCase
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.feed.presentation.screen.feed.state.FeedUiEffect
import com.pegio.feed.presentation.screen.feed.state.FeedUiEvent
import com.pegio.feed.presentation.screen.feed.state.FeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val fetchCurrentUserStream: FetchCurrentUserStreamUseCase,
    private val fetchRelevantPosts: FetchRelevantPostsUseCase,
    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper
) : BaseViewModel<FeedUiState, FeedUiEffect, FeedUiEvent>(initialState = FeedUiState()) {

    init {
        observeCurrentUser()
        loadMorePosts()
    }

    override fun onEvent(event: FeedUiEvent) {
        when (event) {
            FeedUiEvent.OnCreatePostClick -> sendEffect(FeedUiEffect.NavigateToCreatePost)
            FeedUiEvent.OnLoadMorePosts -> loadMorePosts()

            // Top Bar
            FeedUiEvent.OnDrawerClick -> sendEffect(FeedUiEffect.OpenDrawer)
            FeedUiEvent.OnChatClick -> sendEffect(FeedUiEffect.NavigateToChat)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun observeCurrentUser() {
        fetchCurrentUserStream()
            .onSuccess { updateState { copy(currentUser = uiUserMapper.mapFromDomain(it)) } }
            .launchIn(viewModelScope)
    }

    private fun loadMorePosts() = launchWithLoading {
        fetchRelevantPosts()
            .onSuccess { posts ->
                val fetchedPosts = posts.map(uiPostMapper::mapFromDomain)
                updateState { copy(relevantPosts = relevantPosts.plus(fetchedPosts)) }
            }
    }
}