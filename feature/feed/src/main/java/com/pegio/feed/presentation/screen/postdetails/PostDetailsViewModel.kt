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
import com.pegio.domain.usecase.feed.WriteCommentUseCase
import com.pegio.feed.presentation.model.mapper.UiPostCommentMapper
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.feed.presentation.screen.postdetails.navigation.PostDetailsRoute
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEffect
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiEvent
import com.pegio.feed.presentation.screen.postdetails.state.PostDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val fetchPostById: FetchPostByIdUseCase,
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

            // Compose State
            is PostDetailsUiEvent.OnCommentTextChange -> updateState { copy(commentText = event.value) }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun handleCommentSubmit() = viewModelScope.launch {
        writeComment(content = uiState.commentText, postId = postId)
            .onSuccess { comment ->
                getCurrentUser()
                    .onSuccess { currentUser ->
                        val uiComment =
                            uiPostCommentMapper.mapFromDomain(data = comment to currentUser)
                        updateState { copy(comments = comments.plus(uiComment)) }
                    }
            }
    }

    private fun loadMoreComments() = launchWithLoading {
        fetchNextCommentsPage(postId)
            .onSuccess { fetchedComments ->
                val combinedComments = fetchedComments.map(uiPostCommentMapper::mapFromDomain)
                updateState { copy(comments = comments.plus(combinedComments)) }
            }
            .onFailure { error ->
                when (error) {
                    DataError.Pagination.END_OF_PAGINATION_REACHED ->
                        updateState { copy(endOfCommentsReached = true) }

                    else -> {}
                }
            }
    }

    private fun fetchCurrentPost() = launchWithLoading {
        fetchPostById(id = postId)
            .onSuccess { updateState { copy(displayedPost = uiPostMapper.mapFromDomain(it)) } }
            .onFailure { /* TODO: HANDLE */ }
    }
}