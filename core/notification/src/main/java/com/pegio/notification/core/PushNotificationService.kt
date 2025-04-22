package com.pegio.notification.core

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotificationService : FirebaseMessagingService() {

    private companion object {
        const val POST_ID = "postId"

        const val TITLE = "title"
        const val BODY = "body"
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.data[TITLE]
            ?: message.notification?.title
            ?: "New notification"

        val body = message.data[BODY]
            ?: message.notification?.body
            ?: ""

        val postId = message.data[POST_ID]

        notificationHelper.showNotification(
            title = title,
            body = body,
            postId = postId
        )
    }
}