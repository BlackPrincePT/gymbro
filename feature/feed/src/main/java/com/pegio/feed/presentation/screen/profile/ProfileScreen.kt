package com.pegio.feed.presentation.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEvent
import com.pegio.feed.presentation.screen.profile.state.ProfileUiState

@Composable
internal fun ProfileScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: ProfileViewModel = hiltViewModel()
) {


}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {

}

@Preview
@Composable
private fun ProfileContentPreview() {
    ProfileContent(state = ProfileUiState(), onEvent = { })
}