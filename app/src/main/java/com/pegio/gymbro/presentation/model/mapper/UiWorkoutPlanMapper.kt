package com.pegio.gymbro.presentation.model.mapper

import com.pegio.gymbro.domain.core.FromDomainMapper
import com.pegio.gymbro.domain.model.WorkoutPlan
import com.pegio.gymbro.presentation.model.UiWorkoutPlan
import javax.inject.Inject

class UiWorkoutPlanMapper  @Inject constructor() : FromDomainMapper<UiWorkoutPlan, WorkoutPlan> {

    override fun mapFromDomain(data: WorkoutPlan): UiWorkoutPlan {
        return UiWorkoutPlan(
            id = data.id,
            title = data.title,
            description = data.description,
            duration = data.duration,
            intensity = data.intensity,
            difficulty = data.level.name.lowercase().replaceFirstChar { it.uppercase() },
            imageUrl = data.imageUrl
        )
    }

}