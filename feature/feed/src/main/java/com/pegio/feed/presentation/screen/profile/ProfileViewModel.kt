package com.pegio.feed.presentation.screen.profile

import android.net.Uri
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
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.common.DeleteFileUseCase
import com.pegio.domain.usecase.common.EnqueueFileUploadUseCase
import com.pegio.domain.usecase.common.FetchUserByIdUseCase
import com.pegio.domain.usecase.common.GetCurrentAuthUserUseCase
import com.pegio.domain.usecase.common.SaveUserUseCase
import com.pegio.domain.usecase.feed.DeleteVoteUseCase
import com.pegio.domain.usecase.feed.FetchLatestUserPostsUseCase
import com.pegio.domain.usecase.feed.FollowUserUseCase
import com.pegio.domain.usecase.feed.IsCurrentUserFollowingUseCase
import com.pegio.domain.usecase.feed.ResetPostPaginationUseCase
import com.pegio.domain.usecase.feed.UnfollowUserUseCase
import com.pegio.domain.usecase.feed.VotePostUseCase
import com.pegio.feed.presentation.model.mapper.UiPostMapper
import com.pegio.feed.presentation.screen.profile.navigation.ProfileRoute
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEffect
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEvent
import com.pegio.feed.presentation.screen.profile.state.ProfileUiState
import com.pegio.model.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentAuthUser: GetCurrentAuthUserUseCase,
    private val fetchUserById: FetchUserByIdUseCase,
    private val saveUser: SaveUserUseCase,

    private val fetchLatestUserPosts: FetchLatestUserPostsUseCase,
    private val resetPostPagination: ResetPostPaginationUseCase,

    private val enqueueFileUpload: EnqueueFileUploadUseCase,
    private val deleteFile: DeleteFileUseCase,

    private val votePost: VotePostUseCase,
    private val deleteVote: DeleteVoteUseCase,

    private val followUser: FollowUserUseCase,
    private val unfollowUser: UnfollowUserUseCase,
    private val isCurrentUserFollowing: IsCurrentUserFollowingUseCase,

    private val uiUserMapper: UiUserMapper,
    private val uiPostMapper: UiPostMapper,

    savedStateHandle: SavedStateHandle
) : BaseViewModel<ProfileUiState, ProfileUiEffect, ProfileUiEvent>(initialState = ProfileUiState()) {


    private val userId = savedStateHandle.toRoute<ProfileRoute>().userId

    init {
        resetPostPagination()
        updateProfileOwnershipStatus()
        updateFollowingStatus()
        loadProfileInfo()
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onEvent(event: ProfileUiEvent) {
        when (event) {

            // Main
            is ProfileUiEvent.OnEditModeChange -> updateState { copy(editMode = event.mode) }
            is ProfileUiEvent.OnPostVote -> handlePostVote(event.postId, event.voteType)
            ProfileUiEvent.OnLoadMorePosts -> loadMorePosts()
            ProfileUiEvent.OnPostsRefresh -> refreshPosts()
            ProfileUiEvent.OnFollowClick -> handleFollowClick()

            // Image
            ProfileUiEvent.OnOpenGallery -> sendEffect(ProfileUiEffect.LaunchGallery)
            is ProfileUiEvent.OnAvatarSelected -> uploadAvatar(event.uri)
            is ProfileUiEvent.OnBackgroundSelected -> uploadBackground(event.uri)
            ProfileUiEvent.OnAvatarRemove -> removeAvatar()
            ProfileUiEvent.OnBackgroundRemove -> removeBackground()


            // Navigation
            is ProfileUiEvent.OnFollowRecordClick ->
                sendEffect(ProfileUiEffect.NavigateToFollowRecord(event.userId, event.mode))

            is ProfileUiEvent.OnCreatePostClick ->
                sendEffect(ProfileUiEffect.NavigateToCreatePost(event.shouldOpenGallery))

            is ProfileUiEvent.OnPostCommentClick ->
                sendEffect(ProfileUiEffect.NavigateToPostDetail(event.postId))

            ProfileUiEvent.OnBackClick -> sendEffect(ProfileUiEffect.NavigateBack)

            // Bottom Sheet
            is ProfileUiEvent.OnBottomSheetStateUpdate -> updateState { copy(shouldShowBottomSheet = event.shouldShow) }
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun setAvatarLoading(isLoading: Boolean) =
        updateState { copy(isAvatarLoading = isLoading) }

    private fun setBackgroundLoading(isLoading: Boolean) =
        updateState { copy(isBackgroundLoading = isLoading) }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun loadProfileInfo() = launchWithLoading {
        fetchUserById(id = userId)
            .onSuccess { updateState { copy(displayedUser = uiUserMapper.mapFromDomain(it)) } }
            .onFailure { showDisplayableError(it) }
    }

    private fun updateProfileOwnershipStatus() {
        val currentUser = getCurrentAuthUser()
        updateState { copy(isProfileOwner = currentUser?.id == userId) }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun uploadAvatar(uri: Uri) = launchWithLoading(::setAvatarLoading) {
        with(uiState.displayedUser) {
            avatarUrl?.let { deleteFile(it) }

            enqueueFileUpload(uri = uri.toString())
                .onSuccess { saveAndUpdateUser(uiUser = copy(avatarUrl = it)) }
                .onFailure { showDisplayableError(it) }
        }
    }

    private fun removeAvatar() = with(uiState.displayedUser) {
        avatarUrl?.let {
            deleteFile(it)
            saveAndUpdateUser(uiUser = copy(avatarUrl = null))
        }
    }

    private fun uploadBackground(uri: Uri) = launchWithLoading(::setBackgroundLoading) {
        with(uiState.displayedUser) {
            imgBackgroundUrl?.let { deleteFile(it) }

            enqueueFileUpload(uri = uri.toString())
                .onSuccess { saveAndUpdateUser(uiUser = copy(imgBackgroundUrl = it)) }
                .onFailure { showDisplayableError(it) }
        }
    }

    private fun removeBackground() = with(uiState.displayedUser) {
        imgBackgroundUrl?.let {
            deleteFile(it)
            saveAndUpdateUser(uiUser = copy(imgBackgroundUrl = null))
        }
    }

    private fun saveAndUpdateUser(uiUser: UiUser) {
        saveUser(user = uiUserMapper.mapToDomain(uiUser))
        updateState { copy(displayedUser = uiUser) }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun handleFollowClick() {
        if (uiState.isFollowing) unfollow()
        else follow()
    }

    private fun follow() {
        followUser(targetUserId = userId)
        updateState { copy(isFollowing = true) }
    }

    private fun unfollow() {
        unfollowUser(targetUserId = userId)
        updateState { copy(isFollowing = false) }
    }

    private fun updateFollowingStatus() = viewModelScope.launch {
        if (uiState.isProfileOwner) return@launch

        isCurrentUserFollowing(targetUserId = userId)
            .onSuccess { updateState { copy(isFollowing = true) } }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun loadMorePosts() {
        val userId = uiState.displayedUser.id
        if (uiState.endOfPostsReached || userId.isEmpty()) return

        launchWithLoading {
            fetchLatestUserPosts(authorId = userId)
                .getOrElse { error ->
                    when (error) {
                        DataError.Pagination.END_OF_PAGINATION_REACHED ->
                            updateState { copy(endOfPostsReached = true) }

                        else -> showDisplayableError(error)
                    }
                    return@launchWithLoading
                }
                .map(uiPostMapper::mapFromDomain)
                .let { updateState { copy(userPosts = it) } }

            updateState { copy(isRefreshing = false) }
        }
    }

    private fun refreshPosts() {
        resetPostPagination()
        updateState {
            copy(userPosts = emptyList(), endOfPostsReached = false, isRefreshing = true)
        }
        loadMorePosts()
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


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
                .onFailure { showDisplayableError(it) }
        } else {
            votePost(postId, voteType)
                .onSuccess { updatePost(index = index, newVote = it, difference = difference) }
                .onFailure { showDisplayableError(it) }
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun updatePost(index: Int, newVote: Vote?, difference: Int) {
        val updatedPosts = uiState.userPosts.toMutableList()
        val post = updatedPosts[index]

        updatedPosts[index] = post.copy(
            currentUserVote = newVote,
            voteCount = (post.voteCount.toInt() + difference).toString()
        )

        updateState { copy(userPosts = updatedPosts) }
    }

    private fun showDisplayableError(error: Error) {
        if (error is Displayable) sendEffect(ProfileUiEffect.ShowSnackbar(error.toStringResId()))
    }
}