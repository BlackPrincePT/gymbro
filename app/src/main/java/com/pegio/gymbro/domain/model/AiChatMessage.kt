package com.pegio.gymbro.domain.model

import com.pegio.gymbro.presentation.core.DateUtil

data class AiChatMessage(
    val id: String = "",
    val text: String,
    val date: String = DateUtil.todayDate(),
    val imageUri : String? = null,
    val isFromUser: Boolean = true,
)
