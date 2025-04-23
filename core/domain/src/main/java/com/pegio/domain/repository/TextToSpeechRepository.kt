package com.pegio.domain.repository

interface TextToSpeechRepository {
    fun speak(text: String)
    fun stop()
    fun shutdown()
}
