package com.pegio.domain.usecase.aichat

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.firestore.repository.AiMessagesRepository
import com.pegio.model.AiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class ObserveAiMessagesPagingStreamUseCase @Inject constructor(
    private val aiMessagesRepository: AiMessagesRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(lastMessageId: Long? = null): Flow<Resource<List<AiMessage>, DataError.Firestore>> {
        return authRepository.getCurrentUserStream()
            .transform { user ->
                if (user?.isAnonymous == true) emit(value = null)
                else emit(user)
            }
            .filterNotNull()
            .flatMapLatest { user ->
                aiMessagesRepository.observeAiMessagesPagingStream(user.id, lastMessageId)
            }
    }
}