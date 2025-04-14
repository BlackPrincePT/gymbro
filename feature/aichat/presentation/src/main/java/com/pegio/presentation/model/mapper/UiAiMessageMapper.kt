package com.pegio.presentation.model.mapper

import com.pegio.model.AiMessage
import com.pegio.presentation.model.UiAiMessage
import com.pegio.common.core.Mapper
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UiAiMessageMapper @Inject constructor() : Mapper<UiAiMessage, AiMessage> {

    override fun mapToDomain(data: UiAiMessage): AiMessage {
        return AiMessage(
            id = data.id,
            text = data.text,
            imageUrl = data.imageUrl,
            isFromUser = data.isFromUser,
            timestamp = System.currentTimeMillis()
        )
    }

    override fun mapFromDomain(data: AiMessage): UiAiMessage {
        return UiAiMessage(
            id = data.id,
            text = data.text,
            imageUrl = data.imageUrl,
            isFromUser = data.isFromUser,
            date = convertEpochToString(data.timestamp)
        )
    }

    private fun convertEpochToString(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        return localDateTime.toJavaLocalDateTime().format(formatter) // FIXME PLEASE
    }
}