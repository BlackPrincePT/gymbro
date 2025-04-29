package com.pegio.domain.usecase.aichat

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.getOrElse
import com.pegio.common.core.retryableCall
import com.pegio.domain.model.AiChatContext
import com.pegio.domain.model.UserContext
import com.pegio.firestore.repository.UserRepository
import com.pegio.model.AiMessage
import com.pegio.network.repository.AiChatRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SendMessageToAiUseCase @Inject constructor(
    private val aiChatRepository: AiChatRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        aiMessages: List<AiMessage>,
        aiChatContext: AiChatContext?
    ): Resource<AiMessage, Error> {
        val currentAuthUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentAuthUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        val currentUser = retryableCall { userRepository.fetchUserById(currentAuthUser.id) }
            .getOrElse { return it.asFailure() }

        val userContext = UserContext(
            username = currentUser.username,
            age = currentUser.age,
            gender = currentUser.gender,
            heightCm = currentUser.heightCm,
            weightKg = currentUser.weightKg
        )

        val json = Json { prettyPrint = true }

        val userContextMsg = json.encodeToString(userContext)
        val aiChatContextMsg = json.encodeToString(aiChatContext)

        val message = aiMessages.last()

        val messageWithContext = message.copy(
            text = """
                message sender info (this is not post author info): $userContextMsg
                post info that the user is asking about (if empty user is not asking): $aiChatContextMsg
                message: ${message.text}
            """.trimIndent()
        )

        val messages = aiMessages
            .dropLast(n = 1)
            .toMutableList()

        messages.add(messageWithContext)

        messages.joinToString().also { println(it.length) }

        return aiChatRepository.sendMessage(aiMessage = messages)
    }
}