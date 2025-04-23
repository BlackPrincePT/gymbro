package com.pegio.workout.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pegio.model.Workout.WorkoutType
import com.pegio.workout.presentation.screen.workout.WorkoutUiState.TimerState

@Composable
fun TimerSection(
    workout: WorkoutType,
    timeRemaining: Int,
    timerState: TimerState,
    onStartTimer: (Int) -> Unit,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit
) {
    if (workout == WorkoutType.TIMED) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Time Remaining: ${timeRemaining}s",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (timerState) {
                    TimerState.STOPPED -> {
                        IconButton(
                            onClick = { onStartTimer(timeRemaining) },
                            modifier = Modifier
                                .size(56.dp)
                                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Start Timer",
                                tint = Color.White
                            )
                        }
                    }

                    TimerState.RUNNING -> {
                        IconButton(
                            onClick = onPauseTimer,
                            modifier = Modifier
                                .size(56.dp)
                                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = "Pause Timer",
                                tint = Color.White
                            )
                        }
                    }

                    TimerState.PAUSED -> {
                        IconButton(
                            onClick = onResumeTimer,
                            modifier = Modifier
                                .size(56.dp)
                                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Resume Timer",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TimerSectionPreview() {
    MaterialTheme {
        TimerSection(
            workout = WorkoutType.TIMED,
            timeRemaining = 45,
            timerState = TimerState.RUNNING,
            onStartTimer = {},
            onPauseTimer = {},
            onResumeTimer = {}
        )
    }
}
