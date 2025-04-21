package com.pegio.feed.presentation.screen.profile

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pegio.common.core.getOrElse
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.domain.usecase.common.FetchUserByIdUseCase
import com.pegio.domain.usecase.feed.DeleteVoteUseCase
import com.pegio.domain.usecase.feed.FetchLatestUserPostsUseCase
import com.pegio.domain.usecase.feed.VotePostUseCase
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.feed.presentation.screen.profile.navigation.ProfileRoute
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEffect
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEvent
import com.pegio.feed.presentation.screen.profile.state.ProfileUiState
import com.pegio.model.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchUserById: FetchUserByIdUseCase,
    private val fetchLatestUserPosts: FetchLatestUserPostsUseCase,
    private val votePost: VotePostUseCase,
    private val deleteVote: DeleteVoteUseCase,
    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ProfileUiState, ProfileUiEffect, ProfileUiEvent>(initialState = ProfileUiState()) {

    private val userId = savedStateHandle.toRoute<ProfileRoute>().userId

    init {
        loadProfileInfo()
    }

    override fun onEvent(event: ProfileUiEvent) {
        when (event) {

            // Main
            is ProfileUiEvent.OnPostVote -> handlePostVote(event.postId, event.voteType)

            // Top Bar
            ProfileUiEvent.OnBackClick -> sendEffect(ProfileUiEffect.NavigateBack)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun handlePostVote(postId: String, voteType: Vote.Type) {
        val index = uiState.userPosts.indexOfFirst { it.id == postId }
        if (index < 0) return

        val post = uiState.userPosts[index]
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

    private fun loadProfileInfo() = launchWithLoading {
        fetchUserById(id = userId)
            .onFailure { return@launchWithLoading } // TODO HANDLE PROPERLY
            .onSuccess { user ->
                updateState { copy(displayedUser = uiUserMapper.mapFromDomain(user)) }

                fetchLatestUserPosts(authorId = user.id)
                    .getOrElse { return@launchWithLoading } // TODO HANDLE PROPERLY
                    .map(uiPostMapper::mapFromDomain)
                    .let { updateState { copy(userPosts = it) } }
            }
    }

    private fun updatePost(index: Int, newVote: Vote?, difference: Int) {
        val updatedPosts = uiState.userPosts.toMutableList()
        val post = updatedPosts[index]

        updatedPosts[index] = post.copy(
            currentUserVote = newVote,
            voteCount = (post.voteCount.toInt() + difference).toString()
        )

        updateState { copy(userPosts = updatedPosts) }
    }
}