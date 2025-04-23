package com.pegio.notification.core

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class NotificationService : FirebaseMessagingService() {

    private companion object {
        const val TYPE = "type"
        const val TITLE = "title"
        const val BODY = "body"
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private sealed interface Notification {
        enum class Type { NEW_POST, NEW_FOLLOW }

        data object NewPost : Notification {
            const val POST_ID = "postId"
        }

        data object NewFollower : Notification {
            const val FOLLOWER_ID = "followerId"
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.data[TITLE] ?: message.notification?.title ?: "New notification"
        val body = message.data[BODY] ?: message.notification?.body.orEmpty()

        when (Notification.Type.entries.find { it.name == message.data[TYPE] }) {
            Notification.Type.NEW_POST -> notificationHelper.showNewPostNotification(
                title = title,
                body = body,
                postId = message.data[Notification.NewPost.POST_ID]
            )

            Notification.Type.NEW_FOLLOW -> notificationHelper.showNewFollowerNotification(
                title = title,
                body = body,
                followerId = message.data[Notification.NewFollower.FOLLOWER_ID]
            )

            null -> return
        }
    }
}