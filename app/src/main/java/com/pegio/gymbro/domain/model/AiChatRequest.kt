package com.pegio.gymbro.domain.model

import com.pegio.gymbro.BuildConfig


data class AiChatRequest(
    val model:String  = BuildConfig.GPT_MODEL,
    val aiMessages: List<AiMessage>
)