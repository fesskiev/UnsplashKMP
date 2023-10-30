package com.unsplash.shared.domain

import com.unsplash.shared.domain.model.OrderType
import com.unsplash.shared.domain.usecases.GetPhotosUseCase
import com.unsplash.shared.mock.PhotosNetworkSourceMock
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GetPhotosUseCaseTest {

    private lateinit var useCase: GetPhotosUseCase
    private val networkSource = PhotosNetworkSourceMock()

    @BeforeTest
    fun setUp() {
        useCase = GetPhotosUseCase(networkSource)
    }

    @Test
    fun `should return list of photo if response is success`() = runTest {
        // given
        val page = 1
        val orderType = OrderType.LATEST
        networkSource.isSuccessResponse = true

        // when
        val actualResult = useCase.invoke(page, orderType)

        // then
        assertTrue(
            actualResult.isSuccess
        )
        assertTrue(
            actualResult.dataOrNull()?.isNotEmpty() == true
        )
    }

    @Test
    fun `should return error if response is failure`() = runTest {
        // given
        val page = 1
        val orderType = OrderType.LATEST
        networkSource.isSuccessResponse = false

        // when
        val actualResult = useCase.invoke(page, orderType)

        // then
        assertTrue(
            actualResult.isError
        )
        assertTrue(
            actualResult.exceptionOrNull() is IOException
        )
    }

    @Test
    fun `should return error if page lower than 1`() = runTest {
        // given
        val page = 0
        val orderType = OrderType.LATEST
        networkSource.isSuccessResponse = false

        // when
        val actualResult = useCase.invoke(page, orderType)

        // then
        assertTrue(
            actualResult.isError
        )
        assertTrue(
            actualResult.exceptionOrNull() is IllegalArgumentException
        )
    }
}