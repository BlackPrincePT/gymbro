package com.pegio.settings.presentation.screen.settings

import androidx.lifecycle.viewModelScope
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.designsystem.theme.ThemeMode
import com.pegio.domain.usecase.common.CacheThemeModeUseCase
import com.pegio.domain.usecase.common.ObserveCachedThemeModeUseCase
import com.pegio.settings.presentation.screen.settings.state.SettingsUiEffect
import com.pegio.settings.presentation.screen.settings.state.SettingsUiEvent
import com.pegio.settings.presentation.screen.settings.state.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val cacheThemeMode: CacheThemeModeUseCase,
    private val observeCachedThemeMode: ObserveCachedThemeModeUseCase
) : BaseViewModel<SettingsUiState, SettingsUiEffect, SettingsUiEvent>(initialState = SettingsUiState()) {


    init {
        observeCurrentTheme()
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onEvent(event: SettingsUiEvent) {
        when (event) {

            // Main
            is SettingsUiEvent.OnThemeSelected -> handleThemeSelection(event.mode)

            // Navigation
            SettingsUiEvent.OnBackClick -> sendEffect(SettingsUiEffect.NavigateBack)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun handleThemeSelection(mode: ThemeMode) {
        launchWithLoading {
            cacheThemeMode(value = mode.name)
        }
    }

    private fun observeCurrentTheme() = viewModelScope.launch {
        observeCachedThemeMode()
            .filterNotNull()
            .collectLatest { updateState { copy(selectedMode = ThemeMode.valueOf(it)) } }
    }
}