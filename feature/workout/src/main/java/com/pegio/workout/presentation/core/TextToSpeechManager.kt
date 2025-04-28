package com.pegio.workout.presentation.core

interface TextToSpeechManager {
    fun speak(text: String)
    fun stop()
    fun shutdown()
}
