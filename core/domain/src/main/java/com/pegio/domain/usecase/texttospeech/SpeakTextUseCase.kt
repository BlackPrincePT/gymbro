package com.pegio.domain.usecase.texttospeech

import com.pegio.domain.repository.TextToSpeechRepository
import javax.inject.Inject

class SpeakTextUseCase @Inject constructor(
    private val textToSpeechRepository: TextToSpeechRepository
) {
    operator fun invoke(text: String) {
        textToSpeechRepository.speak(text)
    }
}
