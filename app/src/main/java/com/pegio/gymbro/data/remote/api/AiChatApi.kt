package com.pegio.gymbro.data.remote.api

import com.pegio.gymbro.BuildConfig
import com.pegio.gymbro.data.remote.model.AiChatRequestDto
import com.pegio.gymbro.data.remote.model.AiChatResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AiChatApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.API_KEY}"
    )
    @POST(BuildConfig.GPT_ENDPOINT)
    suspend fun sendMessage(@Body request: AiChatRequestDto): Response<AiChatResponseDto>
}