package com.pegio.gymbro.presentation.mapper

import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.presentation.model.UiAiMessage
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