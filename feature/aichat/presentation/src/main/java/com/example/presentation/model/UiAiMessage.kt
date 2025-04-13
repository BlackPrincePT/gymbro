package com.example.presentation.model

data class UiAiMessage(
    val id: String = "",
    val text: String = "",
    val imageUrl : String? = null,
    val isFromUser: Boolean = true,
    val date: String = ""
)