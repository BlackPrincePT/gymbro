package com.example.data.model.mapper


import com.example.model.WorkoutPlan
import com.pegio.common.core.Mapper
import com.pegio.firestore.model.WorkoutPlanDto
import javax.inject.Inject

class WorkoutPlanDtoMapper @Inject constructor() : Mapper<WorkoutPlanDto, WorkoutPlan> {

    override fun mapToDomain(data: WorkoutPlanDto): WorkoutPlan {
        return WorkoutPlan(
            id = data.id,
            level = data.level,
            title = data.title,
            description = data.description,
            duration = data.duration,
            intensity = data.intensity,
            imageUrl = data.imageUrl
        )
    }

    override fun mapFromDomain(data: WorkoutPlan): WorkoutPlanDto {
        return WorkoutPlanDto(
            id = data.id,
            level = data.level,
            title = data.title,
            description = data.description,
            duration = data.duration,
            intensity = data.intensity,
            imageUrl = data.imageUrl
        )
    }
}
