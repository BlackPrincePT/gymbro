package com.pegio.network.repository

interface TextToSpeechRepository {
    fun speak(text: String)
    fun stop()
    fun shutdown()
}
