package com.pegio.domain.usecase.aichat

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.firestore.repository.AiMessagesRepository
import com.pegio.model.AiMessage
import javax.inject.Inject

class SaveFireStoreMessagesUseCase @Inject constructor(
    private val aiMessagesRepository: AiMessagesRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(aiChatMessage: AiMessage): Resource<Unit, SessionError> {
        val currentAuthUsr =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentAuthUsr.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        aiMessagesRepository.saveMessagesInFireStore(
            userId = currentAuthUsr.id,
            aiChatMessage = aiChatMessage
        )

        return Unit.asSuccess()
    }
}