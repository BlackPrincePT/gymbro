package com.pegio.workout

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.domain.usecase.workout.ObserveWorkoutPlansPagingStreamUseCase
import com.pegio.model.WorkoutPlan
import com.pegio.workout.presentation.model.UiWorkoutPlan
import com.pegio.workout.presentation.model.mapper.UiWorkoutPlanMapper
import com.pegio.workout.presentation.screen.workoutplan.state.WorkoutPlanUiEffect
import com.pegio.workout.presentation.screen.workoutplan.state.WorkoutPlanUiEvent
import com.pegio.workout.presentation.screen.workoutplan.WorkoutPlanViewModel
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue


class ExercisePlanViewModelTest {

    private lateinit var viewModel: WorkoutPlanViewModel

    private val observeWorkoutPlansPagingStreamUseCase: ObserveWorkoutPlansPagingStreamUseCase = mockk()
    private val uiWorkoutPlanMapper: UiWorkoutPlanMapper = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = WorkoutPlanViewModel(
            observeWorkoutPlansPagingStreamUseCase,
            uiWorkoutPlanMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given successful use case, when LoadInitialPlans is triggered, then plans are loaded and mapped`() = runTest {
        // Given
        val domainPlans = listOf(mockk<WorkoutPlan>())
        val uiPlans = listOf(mockk<UiWorkoutPlan>())

        coEvery { observeWorkoutPlansPagingStreamUseCase.invoke() } returns flow {
            emit(Resource.Success(domainPlans))
        }
        every { uiWorkoutPlanMapper.mapFromDomain(any()) } returnsMany uiPlans

        // When
        viewModel.onEvent(WorkoutPlanUiEvent.LoadInitialPlans)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState
        assertEquals(uiPlans, state.plans)
        assertFalse(state.isLoading)
    }

    @Test
    fun `given viewModel, when OnBackClick is triggered, then NavigateBack effect is emitted`() = runTest {
        // Given
        val effects = mutableListOf<WorkoutPlanUiEffect>()
        val job = launch { viewModel.uiEffect.toList(effects) }

        // When
        viewModel.onEvent(WorkoutPlanUiEvent.OnBackClick)
        advanceUntilIdle()

        // Then
        assertTrue(effects.contains(WorkoutPlanUiEffect.NavigateBack))
        job.cancel()
    }

    @Test
    fun `given viewModel, when OnInfoClick is triggered, then NavigateToAiChat effect is emitted`() = runTest {
        // Given
        val effects = mutableListOf<WorkoutPlanUiEffect>()
        val job = launch { viewModel.uiEffect.toList(effects) }

        // When
        viewModel.onEvent(WorkoutPlanUiEvent.OnInfoClick)
        advanceUntilIdle()

        // Then
        assertTrue(effects.contains(WorkoutPlanUiEffect.NavigateToAiChat))
        job.cancel()
    }

    @Test
    fun `given use case failure, when LoadInitialPlans is triggered, then failure effect is emitted`() = runTest {
        // Given
        val error = DataError.Firestore.Unknown
        coEvery { observeWorkoutPlansPagingStreamUseCase.invoke() } returns flow {
            emit(Resource.Failure(error))
        }
        val effects = mutableListOf<WorkoutPlanUiEffect>()
        val job = launch { viewModel.uiEffect.toList(effects) }

        // When
        viewModel.onEvent(WorkoutPlanUiEvent.LoadInitialPlans)
        advanceUntilIdle()

        // Then
        assertTrue(effects.any { it is WorkoutPlanUiEffect.Failure })
        job.cancel()
    }
}
