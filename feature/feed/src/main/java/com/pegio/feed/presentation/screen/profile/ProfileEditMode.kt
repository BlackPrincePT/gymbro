package com.pegio.feed.presentation.screen.profile

import android.net.Uri
import com.pegio.feed.presentation.screen.profile.state.ProfileUiEvent

sealed class ProfileEditMode(
    val getUploadEvent: (Uri) -> ProfileUiEvent,
    val getRemoveEvent: () -> ProfileUiEvent
) {
    data object Avatar :
        ProfileEditMode(
            getUploadEvent = { ProfileUiEvent.OnAvatarSelected(it) },
            getRemoveEvent = { ProfileUiEvent.OnAvatarRemove }
        )

    data object Background :
        ProfileEditMode(
            getUploadEvent = { ProfileUiEvent.OnBackgroundSelected(it) },
            getRemoveEvent = { ProfileUiEvent.OnBackgroundRemove }
        )
}