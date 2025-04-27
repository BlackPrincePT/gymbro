package com.pegio.feed.presentation.screen.feed

import androidx.lifecycle.viewModelScope
import com.pegio.common.core.DataError
import com.pegio.common.core.Displayable
import com.pegio.common.core.Error
import com.pegio.common.core.getOrElse
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.common.GetCurrentUserStreamUseCase
import com.pegio.domain.usecase.feed.DeleteVoteUseCase
import com.pegio.domain.usecase.feed.FetchNextRelevantPostsPageUseCase
import com.pegio.domain.usecase.feed.ResetPostPaginationUseCase
import com.pegio.domain.usecase.feed.VotePostUseCase
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.feed.presentation.screen.feed.state.FeedUiEffect
import com.pegio.feed.presentation.screen.feed.state.FeedUiEvent
import com.pegio.feed.presentation.screen.feed.state.FeedUiState
import com.pegio.model.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getCurrentUserStream: GetCurrentUserStreamUseCase,

    private val fetchNextRelevantPostsPage: FetchNextRelevantPostsPageUseCase,
    private val resetPostPagination: ResetPostPaginationUseCase,

    private val votePost: VotePostUseCase,
    private val deleteVote: DeleteVoteUseCase,

    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper
) : BaseViewModel<FeedUiState, FeedUiEffect, FeedUiEvent>(initialState = FeedUiState()) {

    init {
        refreshPosts()
        observeCurrentUser()
    }

    override fun onEvent(event: FeedUiEvent) {
        when (event) {

            // Main
            FeedUiEvent.OnLoadMorePosts -> loadMorePosts()
            FeedUiEvent.OnPostsRefresh -> refreshPosts()
            is FeedUiEvent.OnPostVote -> handlePostVote(event.postId, event.voteType)

            // Navigation
            is FeedUiEvent.OnCreatePostClick -> sendEffect(FeedUiEffect.NavigateToCreatePost(event.shouldOpenGallery))
            is FeedUiEvent.OnPostCommentClick -> sendEffect(FeedUiEffect.NavigateToPostDetails(event.postId))
            is FeedUiEvent.OnUserProfileClick -> sendEffect(FeedUiEffect.NavigateToUserProfile(event.userId))

            // Top Bar
            FeedUiEvent.OnDrawerClick -> sendEffect(FeedUiEffect.OpenDrawer)
            FeedUiEvent.OnChatClick -> sendEffect(FeedUiEffect.NavigateToChat)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun observeCurrentUser() = viewModelScope.launch {
        getCurrentUserStream()
            .onSuccess { updateState { copy(currentUser = uiUserMapper.mapFromDomain(it)) } }
            .onFailure { updateState { copy(currentUser = null) } }
            .launchIn(viewModelScope)
    }


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun handlePostVote(postId: String, voteType: Vote.Type) {
        val index = uiState.relevantPosts.indexOfFirst { it.id == postId }
        if (index < 0) return

        val post = uiState.relevantPosts[index]
        val previousVoteType = post.currentUserVote?.type

        val difference = when (previousVoteType) {
            null -> voteType.value
            voteType -> -previousVoteType.value
            else -> voteType.value - previousVoteType.value
        }

        if (previousVoteType == voteType) {
            deleteVote(postId)
                .onSuccess { updatePost(index = index, newVote = null, difference = difference) }
                .onFailure { showDisplayableError(it) }
        } else {
            votePost(postId, voteType)
                .onSuccess { updatePost(index = index, newVote = it, difference = difference) }
                .onFailure { showDisplayableError(it) }
        }
    }


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun refreshPosts() {
        resetPostPagination()
        updateState {
            copy(relevantPosts = emptyList(), endOfPostsReached = false, isRefreshing = true)
        }
        loadMorePosts()
    }

    private fun loadMorePosts() {
        if (uiState.endOfPostsReached) return

        launchWithLoading {
            fetchNextRelevantPostsPage()
                .getOrElse { error ->
                    when (error) {
                        DataError.Pagination.END_OF_PAGINATION_REACHED ->
                            updateState { copy(endOfPostsReached = true) }

                        else -> showDisplayableError(error)
                    }
                    return@launchWithLoading
                }
                .map(uiPostMapper::mapFromDomain)
                .let { updateState { copy(relevantPosts = relevantPosts.plus(it)) } }

            updateState { copy(isRefreshing = false) }
        }
    }


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun updatePost(index: Int, newVote: Vote?, difference: Int) {
        val updatedPosts = uiState.relevantPosts.toMutableList()
        val post = updatedPosts[index]

        updatedPosts[index] = post.copy(
            currentUserVote = newVote,
            voteCount = (post.voteCount.toInt() + difference).toString()
        )

        updateState { copy(relevantPosts = updatedPosts) }
    }

    private fun showDisplayableError(error: Error) {
        if (error is Displayable) sendEffect(FeedUiEffect.ShowSnackbar(error.toStringResId()))
    }
}