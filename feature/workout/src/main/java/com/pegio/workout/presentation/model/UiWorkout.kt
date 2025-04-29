package com.pegio.workout.presentation.model

data class UiWorkout(
    val id: String,
    val authorId: String,
    val title: String,
    val description: String
) {

    companion object {

        val DEFAULT = UiWorkout(
            id = "",
            authorId = "",
            title = "Cracked workout",
            description = "Aaaaaah I can't feel my legs"
        )
    }
}