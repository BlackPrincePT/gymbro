package com.pegio.gymbro.presentation.screen.account

import java.util.UUID

sealed interface AccountUiEffect {
    data class ProfilePictureUploadStarted(val workId: UUID) : AccountUiEffect
}
