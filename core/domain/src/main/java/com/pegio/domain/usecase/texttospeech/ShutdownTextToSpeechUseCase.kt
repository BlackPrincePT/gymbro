package com.pegio.domain.usecase.texttospeech

import com.pegio.domain.repository.TextToSpeechRepository
import javax.inject.Inject

class ShutdownTextToSpeechUseCase @Inject constructor(
    private val textToSpeechRepository: TextToSpeechRepository
) {
    operator fun invoke() {
        textToSpeechRepository.shutdown()
    }
}
