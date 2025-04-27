package com.pegio.domain.usecase.workout

import com.pegio.firestore.repository.ExerciseRepository
import javax.inject.Inject

class FetchExerciseByIdUseCase@Inject constructor(private val exerciseRepository: ExerciseRepository) {
    suspend operator fun invoke(id:String) = exerciseRepository.fetchExercises(id)
}