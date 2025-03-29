package com.pegio.gymbro.presentation.model

import com.pegio.gymbro.presentation.core.DateUtil

data class UiAiChatMessage(
    val text: String,
    val date: String = DateUtil.todayDate(),
    val imageUri : String? = null,
    val isFromUser: Boolean = true
)