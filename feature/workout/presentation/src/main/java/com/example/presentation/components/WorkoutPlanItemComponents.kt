package com.example.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.model.UiWorkoutPlan
import com.pegio.designsystem.component.WorkoutPlanImage

@Composable
fun WorkoutPlanItemComponents(workoutPlan: UiWorkoutPlan) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            WorkoutPlanImage(
                imageUrl = workoutPlan.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = workoutPlan.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            Text(
                text = workoutPlan.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = workoutPlan.duration,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall

                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = workoutPlan.intensity,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview
@Composable
private fun WorkoutPlanItemView(){
    WorkoutPlanItemComponents(
        workoutPlan =  UiWorkoutPlan(
            id = "1",
            title = "Beginner Plan",
            description = "Designed for those with some experience. Mix of strength and cardio to push your limits.",
            difficulty = "Easy",
            duration = "4 days/week - 45 minutes per session",
            intensity = "Low",
            imageUrl = "https://avatars.githubusercontent.com/u/172249902?v=4"
        ),
    )
}