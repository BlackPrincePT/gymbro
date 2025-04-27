package com.pegio.workout.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pegio.model.Exercise.Type
import com.pegio.workout.presentation.screen.workout.WorkoutUiState.TimerState

@Composable
fun TimerSection(
    workout: Type,
    workoutTime : Int,
    timeRemaining: Int,
    timerState: TimerState,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit
) {
    if (workout == Type.TIMED) {
        val progress = timeRemaining / workoutTime.toFloat()

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(140.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 6.dp,
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )
                Text(
                    text = "${timeRemaining}s",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            TimerControlButton(
                timerState = timerState,
                onPause = onPauseTimer,
                onResume = onResumeTimer
            )
        }
    }
}

@Composable
private fun TimerControlButton(
    timerState: TimerState,
    onPause: () -> Unit,
    onResume: () -> Unit
) {
    val icon: ImageVector
    val description: String
    val onClick: () -> Unit

    when (timerState) {

        TimerState.RUNNING -> {
            icon = Icons.Default.Pause
            description = "Pause Timer"
            onClick = onPause
        }

        TimerState.PAUSED -> {
            icon = Icons.Default.PlayArrow
            description = "Resume Timer"
            onClick = onResume
        }
    }

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(46.dp)
            .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun TimerSectionPreview() {
    MaterialTheme {
        TimerSection(
            workout = Type.TIMED,
            timeRemaining = 45,
            workoutTime = 10,
            timerState = TimerState.RUNNING,
            onPauseTimer = {},
            onResumeTimer = {}
        )
    }
}
