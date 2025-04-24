package com.pegio.feed.presentation.screen.feed

import androidx.lifecycle.viewModelScope
import com.pegio.common.core.DataError
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val fetchCurrentUserStream: GetCurrentUserStreamUseCase,
    private val fetchNextRelevantPostsPage: FetchNextRelevantPostsPageUseCase,
    private val votePost: VotePostUseCase,
    private val deleteVote: DeleteVoteUseCase,
    private val resetPagination: ResetPostPaginationUseCase,
    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper
) : BaseViewModel<FeedUiState, FeedUiEffect, FeedUiEvent>(initialState = FeedUiState()) {

    init {
        observeCurrentUser()
    }

    override fun onEvent(event: FeedUiEvent) {
        when (event) {

            // Main
            FeedUiEvent.OnLoadMorePosts -> loadMorePosts()
            FeedUiEvent.OnPostsRefresh -> {} // FIXME DOESN'T WORK
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

    private fun observeCurrentUser() = viewModelScope.launch {
        fetchCurrentUserStream()
            .collectLatest { updateState { copy(currentUser = uiUserMapper.mapFromDomain(it)) } }
    }

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
                .onFailure { } // TODO HANDLE FAILURE
        } else {
            votePost(postId, voteType)
                .onSuccess { updatePost(index = index, newVote = it, difference = difference) }
                .onFailure { } // TODO HANDLE FAILURE
        }
    }


//    private fun refreshPosts() {
//        updateState { copy(relevantPosts = emptyList(), endOfPostsReached = false) }
//        resetPagination()
//        loadMorePosts()
//    }

    private fun loadMorePosts() {
        if (uiState.endOfPostsReached) return

        launchWithLoading {
            fetchNextRelevantPostsPage()
                .onSuccess { fetchedPosts ->
                    val combinedPosts = fetchedPosts.map(uiPostMapper::mapFromDomain)
                    updateState { copy(relevantPosts = relevantPosts.plus(combinedPosts)) }
                }
                .onFailure { error ->
                    when (error) {
                        DataError.Pagination.END_OF_PAGINATION_REACHED ->
                            updateState { copy(endOfPostsReached = true) }

                        else -> {} // TODO HANDLE BETTER
                    }
                }
        }
    }

    private fun updatePost(index: Int, newVote: Vote?, difference: Int) {
        val updatedPosts = uiState.relevantPosts.toMutableList()
        val post = updatedPosts[index]

        updatedPosts[index] = post.copy(
            currentUserVote = newVote,
            voteCount = (post.voteCount.toInt() + difference).toString()
        )

        updateState { copy(relevantPosts = updatedPosts) }
    }
}