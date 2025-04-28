package com.pegio.aichat.presentation.model.mapper

import com.pegio.aichat.presentation.model.UiAiMessage
import com.pegio.common.core.Mapper
import com.pegio.common.presentation.util.DateUtils
import com.pegio.model.AiMessage
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
            date = DateUtils.formatRelativeTime(data.timestamp)
        )
    }
}