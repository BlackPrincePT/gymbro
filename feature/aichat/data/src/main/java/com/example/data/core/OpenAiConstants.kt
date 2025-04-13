package com.example.data.core

object OpenAiConstants {
    const val GPT_MODEL = "gpt-4o-mini"
    const val MAX_TOKENS = 300
    const val DEFAULT_SYSTEM_MESSAGE = "You are gym assistant in app called gymBro"

    /**
     *  Role
     */
    const val USER = "user"
    const val ASSISTANT = "assistant"
    const val SYSTEM = "system"

    /**
     *  Content Type
     */
    const val TEXT = "text"
    const val IMAGE_URL = "image_url"
}