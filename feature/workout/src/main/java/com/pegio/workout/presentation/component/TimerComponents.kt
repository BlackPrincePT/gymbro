package com.pegio.workout.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pegio.model.Exercise.Type
import com.pegio.workout.presentation.screen.workout.state.WorkoutUiState.TimerState

@Composable
fun TimerSection(
    workout: Type,
    workoutTime: Int,
    timeRemaining: Int,
    timerState: TimerState,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onResetTimer: () -> Unit
) {
    if (workout == Type.TIMED) {
        val progress = timeRemaining / workoutTime.toFloat()

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
        ) {

            if (timerState != TimerState.NOT_STARTED) {
                IconButton(
                    onClick = {
                        if (timerState == TimerState.RUNNING) onPauseTimer()
                        else onResumeTimer()
                    },
                ) {
                    Icon(
                        imageVector = if (timerState == TimerState.PAUSED)  Icons.Default.PlayArrow else  Icons.Default.Pause ,
                        contentDescription = "Pause Timer",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(170.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onResumeTimer()
                    }
            ) {

                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 7.dp,
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )

                when (timerState) {
                    TimerState.NOT_STARTED -> {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start Timer",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    TimerState.RUNNING -> {
                        Text(
                            text = "${timeRemaining}s",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    TimerState.PAUSED -> {
                        Text(
                            text = "${timeRemaining}s",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (timerState != TimerState.NOT_STARTED) {
                IconButton(
                    onClick = onResetTimer,
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Timer",
                        tint = MaterialTheme.colorScheme.primary
                    )
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
            workout = Type.TIMED,
            timeRemaining = 45,
            workoutTime = 110,
            timerState = TimerState.RUNNING,
            onPauseTimer = {},
            onResumeTimer = {},
            onResetTimer = {}
        )
    }
}
