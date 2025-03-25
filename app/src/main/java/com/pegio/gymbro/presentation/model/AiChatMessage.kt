package com.pegio.gymbro.presentation.model

import com.pegio.gymbro.presentation.core.DateUtil
import java.util.UUID

data class AiChatMessage(
    val id: UUID = UUID.randomUUID(),
    val text: String,
    val date: String = DateUtil.todayDate(),
    val isFromUser: Boolean = true
)