package com.pegio.feed.presentation.screen.createpost

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pegio.domain.usecase.feed.UploadPostUseCase
import com.pegio.feed.presentation.screen.createpost.navigation.CreatePostRoute
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

class CreatePostViewModelTest {

    private val uploadPostUseCase: UploadPostUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CreatePostViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { savedStateHandle.toRoute<CreatePostRoute>() } returns CreatePostRoute(false)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

}