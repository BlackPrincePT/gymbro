package com.pegio.settings.presentation.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.component.DropdownMenu
import com.pegio.designsystem.theme.ThemeMode
import com.pegio.settings.R
import com.pegio.settings.presentation.screen.account.state.AccountUiEvent
import com.pegio.settings.presentation.screen.settings.state.SettingsUiEffect
import com.pegio.settings.presentation.screen.settings.state.SettingsUiEvent
import com.pegio.settings.presentation.screen.settings.state.SettingsUiState

@Composable
internal fun SettingsScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {

            // Navigation
            SettingsUiEffect.NavigateBack -> onBackClick()
        }
    }

    SettingsContent(state = viewModel.uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit
) = with(state) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DropdownMenu(
            options = ThemeMode.entries,
            selectedOption = selectedMode?.name,
            onSelectionChanged = { onEvent(SettingsUiEvent.OnThemeSelected(it)) },
            label = stringResource(R.string.feature_settings_theme),
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (SettingsUiEvent) -> Unit
) {
    val title = stringResource(R.string.feature_settings_settings)

    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                title = title,
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(SettingsUiEvent.OnBackClick) }
                )
            )
        )
    }
}


// <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


@Preview(showBackground = true)
@Composable
private fun SettingsContentPreview() {
    SettingsContent(state = SettingsUiState(), onEvent = { })
}