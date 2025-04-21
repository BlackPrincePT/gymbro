package com.pegio.feed.presentation.screen.postdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pegio.common.core.DataError
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.domain.usecase.common.GetCurrentUserUseCase
import com.pegio.domain.usecase.feed.FetchNextCommentsPageUseCase
import com.pegio.domain.usecase.feed.FetchPostByIdUseCase
import com.pegio.domain.usecase.feed.VotePostUseCase
import com.pegio.domain.usecase.feed.WriteCommentUseCase
import com.pegio.feed.presentation.model.mapper.UiPostCommentMapper
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.feed.presentation.screen.postdetails.navigation.PostDetailsRoute
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEffect
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEvent
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiState
import com.pegio.model.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val fetchPostById: FetchPostByIdUseCase,
    private val votePost: VotePostUseCase,
    private val writeComment: WriteCommentUseCase,
    private val fetchNextCommentsPage: FetchNextCommentsPageUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val uiPostMapper: UiPostMapper,
    private val uiPostCommentMapper: UiPostCommentMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<PostDetailsUiState, PostDetailsUiEffect, PostDetailsUiEvent>(initialState = PostDetailsUiState()) {

    private val postId = savedStateHandle.toRoute<PostDetailsRoute>().postId

    init {
        fetchCurrentPost()
        loadMoreComments()
    }

    override fun onEvent(event: PostDetailsUiEvent) {
        when (event) {

            // Navigation
            PostDetailsUiEvent.OnBackClick -> sendEffect(PostDetailsUiEffect.NavigateBack)

            // Main
            PostDetailsUiEvent.OnCommentSubmitClick -> handleCommentSubmit()
            PostDetailsUiEvent.OnLoadMoreCommentsClick -> loadMoreComments()
            is PostDetailsUiEvent.OnPostVote -> handlePostVote(event.voteType)

            // Compose State
            is PostDetailsUiEvent.OnCommentTextChange -> updateState { copy(commentText = event.value) }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun setLoadingMoreComments(isLoading: Boolean) =
        updateState { copy(loadingMoreComments = isLoading) }

    private fun handlePostVote(voteType: Vote.Type) {
        val post = uiState.displayedPost
        var updatedVoteCount = post.voteCount.toInt() + voteType.value

        if (post.currentUserVote != null)
            updatedVoteCount -= post.currentUserVote.type.value

        votePost(postId, voteType)
            .onFailure { } // TODO HANDLE FAILURE
            .onSuccess { vote ->
                val updatedPost =
                    post.copy(currentUserVote = vote, voteCount = updatedVoteCount.toString())

                updateState { copy(displayedPost = updatedPost) }
            }
    }

    private fun handleCommentSubmit() = viewModelScope.launch {
        writeComment(content = uiState.commentText, postId = postId)
            .onSuccess { comment ->
                updateState { copy(commentText = "") }
                getCurrentUser()
                    .onSuccess { currentUser ->
                        val uiComment =
                            uiPostCommentMapper.mapFromDomain(data = comment to currentUser)
                        updateState { copy(comments = listOf(uiComment) + comments) }
                    }
            }
    }

    private fun loadMoreComments() {
        if (uiState.endOfCommentsReached) return

        launchWithLoading(::setLoadingMoreComments) {
            fetchNextCommentsPage(postId)
                .onSuccess { fetchedComments ->
                    val combinedComments = fetchedComments.map(uiPostCommentMapper::mapFromDomain)
                    updateState { copy(comments = comments.plus(combinedComments)) }
                }
                .onFailure { error ->
                    when (error) {
                        DataError.Pagination.END_OF_PAGINATION_REACHED ->
                            updateState { copy(endOfCommentsReached = true) }

                        else -> {} // TODO HANDLE BETTER
                    }
                }
        }
    }

    private fun fetchCurrentPost() = launchWithLoading {
        fetchPostById(id = postId)
            .onSuccess { updateState { copy(displayedPost = uiPostMapper.mapFromDomain(it)) } }
            .onFailure { /* TODO: HANDLE */ }
    }
}