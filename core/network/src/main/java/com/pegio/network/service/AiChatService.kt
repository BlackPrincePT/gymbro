package com.pegio.network.service

import com.pegio.network.model.AiChatRequestDto
import com.pegio.network.model.AiChatResponseDto
import com.pegio.network.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AiChatService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.API_KEY}"
    )
    @POST(BuildConfig.GPT_ENDPOINT)
    suspend fun sendMessage(@Body request: AiChatRequestDto): Response<AiChatResponseDto>
}