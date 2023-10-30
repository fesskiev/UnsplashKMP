package com.unsplash.shared.presentation

import com.unsplash.shared.domain.usecases.GetPhotosUseCase
import com.unsplash.shared.domain.usecases.RefreshPhotosUseCase
import com.unsplash.shared.mock.PhotosNetworkSourceMock
import com.unsplash.shared.presentation.photos.list.PhotoListContract.Event
import com.unsplash.shared.presentation.photos.list.PhotoListContract.State
import com.unsplash.shared.presentation.photos.list.PhotoListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoListViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: PhotoListViewModel
    private val networkSource = PhotosNetworkSourceMock()
    private val getPhotosUseCase = GetPhotosUseCase(networkSource)
    private val refreshPhotosUseCase = RefreshPhotosUseCase(networkSource)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PhotoListViewModel(getPhotosUseCase, refreshPhotosUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return error if page lower than 1`() = runTest {

        //when
        viewModel.sendViewEvent(Event.GetFirstPhotosPageAction)

        // then
        val actualState = viewModel.viewState.value

        val expectedState = State()
        assertEquals(
            expectedState,
            actualState
        )
    }
}