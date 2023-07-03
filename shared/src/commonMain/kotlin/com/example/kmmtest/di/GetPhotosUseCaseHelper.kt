package com.example.kmmtest.di

import com.example.kmmtest.domain.GetPhotosUseCase
import com.example.kmmtest.network.photos.model.OrderType
import com.example.kmmtest.network.photos.model.PhotoResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPhotosUseCaseHelper(private val page: Int) : KoinComponent {
    private val getPhotosUseCase: GetPhotosUseCase by inject()
    suspend fun getPhotos() : Result<List<PhotoResponse>> = getPhotosUseCase.invoke(page, OrderType.LATEST)
}