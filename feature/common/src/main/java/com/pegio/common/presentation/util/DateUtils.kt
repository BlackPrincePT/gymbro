package com.pegio.common.presentation.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object DateUtils {
    fun formatRelativeTime(epochMillis: Long): String {
        val now = Clock.System.now()
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val duration = now - instant

        return when {
            duration < 60.seconds -> "Just now"
            duration < 60.minutes -> "${duration.inWholeMinutes} ${pluralize(duration.inWholeMinutes, "min")} ago"
            duration < 24.hours -> "${duration.inWholeHours} ${pluralize(duration.inWholeHours, "hr")} ago"
            duration < 2.days -> "Yesterday"
            duration < 7.days -> "${duration.inWholeDays} days ago"
            else -> {
                // Return full date for posts older than 7 days
                val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                localDateTime.toJavaLocalDateTime().format(formatter)
            }
        }
    }

    private fun pluralize(count: Long, singular: String): String {
        return if (count == 1L) singular else "${singular}s"
    }
}