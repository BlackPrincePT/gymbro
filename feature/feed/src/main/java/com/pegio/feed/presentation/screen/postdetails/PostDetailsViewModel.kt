package com.pegio.feed.presentation.screen.postdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pegio.common.core.DataError
import com.pegio.common.core.Displayable
import com.pegio.common.core.Error
import com.pegio.common.core.getOrElse
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.common.GetCurrentUserUseCase
import com.pegio.domain.usecase.feed.DeleteVoteUseCase
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
    private val getCurrentUser: GetCurrentUserUseCase,

    private val fetchPostById: FetchPostByIdUseCase,

    private val votePost: VotePostUseCase,
    private val deleteVote: DeleteVoteUseCase,

    private val writeComment: WriteCommentUseCase,
    private val fetchNextCommentsPage: FetchNextCommentsPageUseCase,

    private val uiPostMapper: UiPostMapper,
    private val uiPostCommentMapper: UiPostCommentMapper,

    savedStateHandle: SavedStateHandle
) : BaseViewModel<PostDetailsUiState, PostDetailsUiEffect, PostDetailsUiEvent>(initialState = PostDetailsUiState()) {


    private val postId = savedStateHandle.toRoute<PostDetailsRoute>().postId

    init {
        fetchCurrentPost()
        loadMoreComments()
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onEvent(event: PostDetailsUiEvent) {
        when (event) {

            // Main
            PostDetailsUiEvent.OnCommentSubmitClick -> handleCommentSubmit()
            PostDetailsUiEvent.OnLoadMoreCommentsClick -> loadMoreComments()
            is PostDetailsUiEvent.OnPostVote -> handlePostVote(event.voteType)

            // Navigation
            PostDetailsUiEvent.OnBackClick -> sendEffect(PostDetailsUiEffect.NavigateBack)
            is PostDetailsUiEvent.OnUserProfileClick -> sendEffect(
                PostDetailsUiEffect.NavigateToUserProfile(
                    event.userId
                )
            )

            // Compose State
            is PostDetailsUiEvent.OnCommentTextChange -> updateState { copy(commentText = event.value) }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun setLoadingMoreComments(isLoading: Boolean) =
        updateState { copy(loadingMoreComments = isLoading) }

    private fun setLoadingSendComments(isLoading: Boolean) =
        updateState { copy(loadingSendComment = isLoading) }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun fetchCurrentPost() = launchWithLoading {
        fetchPostById(id = postId)
            .onSuccess { updateState { copy(displayedPost = uiPostMapper.mapFromDomain(it)) } }
            .onFailure { showDisplayableError(it) }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun handlePostVote(voteType: Vote.Type) {
        val post = uiState.displayedPost
        val previousVoteType = post.currentUserVote?.type.also { println(it) }

        val difference = when (previousVoteType) {
            null -> voteType.value
            voteType -> -previousVoteType.value
            else -> voteType.value - previousVoteType.value
        }

        if (previousVoteType == voteType) {
            deleteVote(postId)
                .onSuccess { updatePost(newVote = null, difference = difference) }
                .onFailure { showDisplayableError(it) }
        } else {
            votePost(postId, voteType)
                .onSuccess { updatePost(newVote = it, difference = difference) }
                .onFailure { showDisplayableError(it) }
        }
    }

    private fun handleCommentSubmit() = launchWithLoading(::setLoadingSendComments) {
        writeComment(content = uiState.commentText, postId = postId)
            .onFailure { showDisplayableError(it) }
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
                .getOrElse { error ->
                    when (error) {
                        DataError.Pagination.END_OF_PAGINATION_REACHED ->
                            updateState { copy(endOfCommentsReached = true) }

                        else -> showDisplayableError(error)
                    }
                    return@launchWithLoading
                }
                .map(uiPostCommentMapper::mapFromDomain)
                .let { updateState { copy(comments = comments.plus(it)) } }
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun updatePost(newVote: Vote?, difference: Int) = with(uiState.displayedPost) {
        val updatedPost = copy(
            currentUserVote = newVote,
            voteCount = (voteCount.toInt() + difference).toString()
        )

        updateState { copy(displayedPost = updatedPost) }
    }

    private fun showDisplayableError(error: Error) {
        if (error is Displayable) sendEffect(PostDetailsUiEffect.ShowSnackbar(error.toStringResId()))
    }
}