package com.pegio.presentation.model.mapper

import com.pegio.model.WorkoutPlan
import com.pegio.presentation.model.UiWorkoutPlan
import com.pegio.common.core.FromDomainMapper
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