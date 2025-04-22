package com.pegio.notification.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.pegio.notification.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(@ApplicationContext context: Context) {

    private companion object {
        const val CHANNEL_POST_ID = "post_notifications"
    }

    private val appContext = context.applicationContext

    private val notificationManager =
        context.getSystemService<NotificationManager>() ?: throw IllegalStateException()

    init {
        setupNotificationChannels()
    }

    private fun setupNotificationChannels() {
        if (notificationManager.getNotificationChannel(CHANNEL_POST_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_POST_ID,
                appContext.getString(R.string.core_notification_channel_post_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.apply {
                description = appContext.getString(R.string.core_notification_channel_post_desc)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        title: String,
        body: String,
        postId: String?
    ) {
        setupNotificationChannels()

        val notification = NotificationCompat.Builder(appContext, CHANNEL_POST_ID)
            .setSmallIcon(R.drawable.core_notification_ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification) // FIXME ID
    }
}





