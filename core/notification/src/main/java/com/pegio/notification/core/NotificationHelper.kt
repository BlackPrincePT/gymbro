package com.pegio.notification.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import com.pegio.notification.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(@ApplicationContext context: Context) {

    private companion object {
        const val CONTENT_CHANNEL = "content_notifications"
        const val SOCIAL_CHANNEL = "social_notifications"
        const val BASE_PATH = "gymbro://"
    }

    private val appContext = context.applicationContext

    private val notificationManager =
        context.getSystemService<NotificationManager>() ?: throw IllegalStateException()

    init {
        setupContentNotificationChannelIfNeeded()
        setupSocialNotificationChannelIfNeeded()
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    fun showNewPostNotification(title: String, body: String, postId: String?) {
        postId?.let { safePostId ->
            val path = "${BASE_PATH}post-details/$safePostId"

            val requestCode = safePostId.hashCode()

            val pendingIntent = createPendingIntent(Uri.parse(path), requestCode)

            showNotification(title, body, pendingIntent, requestCode, CONTENT_CHANNEL)
        }
    }

    fun showNewFollowerNotification(title: String, body: String, followerId: String?) {
        followerId?.let { safeFollowerId ->
            val path = "${BASE_PATH}profile/$safeFollowerId"

            val requestCode = safeFollowerId.hashCode()

            val pendingIntent = createPendingIntent(Uri.parse(path), requestCode)

            showNotification(title, body, pendingIntent, requestCode, SOCIAL_CHANNEL)
        }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun showNotification(
        title: String,
        body: String,
        pendingIntent: PendingIntent?,
        requestCode: Int,
        channelId: String
    ) {

        val notification = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.drawable.core_notification_ic_notification) // FIXME: ADD PROPER ICON
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(requestCode, notification)
    }

    private fun createPendingIntent(uri: Uri, requestCode: Int): PendingIntent? {
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        return TaskStackBuilder.create(appContext)
            .run {
                addNextIntentWithParentStack(createDeepLinkIntent(uri))
                getPendingIntent(requestCode, flags)
            }
    }

    private fun createDeepLinkIntent(uri: Uri): Intent {
        return Intent(Intent.ACTION_VIEW, uri)
            .apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                addCategory(Intent.CATEGORY_BROWSABLE)

                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
    }


    // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


    private fun setupSocialNotificationChannelIfNeeded() {
        if (notificationManager.getNotificationChannel(SOCIAL_CHANNEL) == null) {
            val channel = NotificationChannel(
                SOCIAL_CHANNEL,
                appContext.getString(R.string.core_notification_channel_social_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.apply {
                description = appContext.getString(R.string.core_notification_channel_social_desc)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupContentNotificationChannelIfNeeded() {
        if (notificationManager.getNotificationChannel(CONTENT_CHANNEL) == null) {
            val channel = NotificationChannel(
                CONTENT_CHANNEL,
                appContext.getString(R.string.core_notification_channel_content_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.apply {
                description = appContext.getString(R.string.core_notification_channel_content_desc)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }
}





