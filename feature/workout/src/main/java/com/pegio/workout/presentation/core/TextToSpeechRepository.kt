package com.pegio.workout.presentation.core

interface TextToSpeechRepository {
    fun speak(text: String)
    fun stop()
    fun shutdown()
}
