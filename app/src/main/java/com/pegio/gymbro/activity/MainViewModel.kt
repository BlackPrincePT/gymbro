package com.pegio.gymbro.activity

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.designsystem.theme.ThemeMode
import com.pegio.domain.usecase.account.SignOutUseCase
import com.pegio.domain.usecase.app.LinkAnonymousAccountUseCase
import com.pegio.domain.usecase.common.GetCurrentUserStreamUseCase
import com.pegio.domain.usecase.common.ObserveCachedThemeModeUseCase
import com.pegio.gymbro.activity.state.MainActivityUiEffect
import com.pegio.gymbro.activity.state.MainActivityUiEvent
import com.pegio.gymbro.activity.state.MainActivityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUserStream: GetCurrentUserStreamUseCase,
    private val linkAnonymousAccount: LinkAnonymousAccountUseCase,
    private val signOut: SignOutUseCase,
    private val observeCachedThemeMode: ObserveCachedThemeModeUseCase,
    private val uiUserMapper: UiUserMapper
) : BaseViewModel<MainActivityUiState, MainActivityUiEffect, MainActivityUiEvent>(initialState = MainActivityUiState()) {


    init {
        observeCurrentUser()
        observeCurrentTheme()
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onEvent(event: MainActivityUiEvent) {
        when (event) {

            // Top Bar
            is MainActivityUiEvent.OnUpdateTopBarState -> updateState { copy(topBarState = event.value) }

            // Auth
            is MainActivityUiEvent.LinkAnonymousAccount -> handleLinkAnonymousAccount(event.context)
            MainActivityUiEvent.OnSignOutClick -> handleSignOut()

            // Navigation
            MainActivityUiEvent.OnOpenDrawerClick -> sendEffect(MainActivityUiEffect.OpenDrawer)
            MainActivityUiEvent.OnAccountClick -> sendDrawerEffect(MainActivityUiEffect.NavigateToAccount)
            MainActivityUiEvent.OnSettingsClick -> sendDrawerEffect(MainActivityUiEffect.NavigateToSettings)
            MainActivityUiEvent.OnWorkoutPlanClick -> sendDrawerEffect(MainActivityUiEffect.NavigateToWorkoutPlan)
            MainActivityUiEvent.OnProfileClick -> handleProfileClick()
        }
    }

    override fun setLoading(isLoading: Boolean) {}


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun observeCurrentUser() {
        getCurrentUserStream()
            .onFailure { updateState { copy(currentUser = null, isAnonymous = true) } }
            .onSuccess {
                val mappedUser = uiUserMapper.mapFromDomain(it)
                updateState { copy(currentUser = mappedUser, isAnonymous = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCurrentTheme() = viewModelScope.launch {
        observeCachedThemeMode()
            .filterNotNull()
            .collectLatest { updateState { copy(themeMode = ThemeMode.valueOf(it)) } }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun handleSignOut() {
        signOut()
        sendDrawerEffect(MainActivityUiEffect.NavigateToAuth)
    }

    private fun handleLinkAnonymousAccount(context: Context) = launchWithLoading {
        linkAnonymousAccount(context)
            .onSuccess {
                updateState { copy(isAnonymous = false) }
                sendDrawerEffect(MainActivityUiEffect.NavigateToRegister)
            }
    }

    private fun handleProfileClick() {
        val currentUser = uiState.currentUser ?: return
        sendDrawerEffect(MainActivityUiEffect.NavigateToProfile(userId = currentUser.id))
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun sendDrawerEffect(drawerEffect: MainActivityUiEffect) {
        sendEffect(drawerEffect)
        sendEffect(MainActivityUiEffect.CloseDrawer)
    }
}