package com.pegio.workout.presentation.model.mapper

import com.pegio.model.WorkoutPlan
import com.pegio.workout.presentation.model.UiWorkoutPlan
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
            difficulty = data.level.name.lowercase(),
            imageUrl = data.imageUrl
        )
    }

}