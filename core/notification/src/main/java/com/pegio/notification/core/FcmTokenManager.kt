package com.pegio.notification.core

import com.google.firebase.messaging.FirebaseMessaging
import com.pegio.auth.repository.AuthRepository
import com.pegio.firestore.repository.FcmTokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor(
    private val authRepository: AuthRepository,
    private val fcmTokenRepository: FcmTokenRepository,
) {

    private val scope = CoroutineScope(context = SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            authRepository.getCurrentUserStream()
                .filterNotNull()
                .distinctUntilChanged { old, new -> old.id == new.id }
                .collect { user ->
                    val token = FirebaseMessaging.getInstance().token.await()
                    fcmTokenRepository.saveToken(token = token, ownerId = user.id)
                }
        }
    }
}