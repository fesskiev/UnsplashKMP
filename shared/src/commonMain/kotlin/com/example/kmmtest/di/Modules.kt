package com.example.kmmtest.di

import com.example.kmmtest.domain.GetPhotoUseCase
import com.example.kmmtest.domain.GetPhotosUseCase
import com.example.kmmtest.network.client.provideKtorClient
import com.example.kmmtest.network.photos.PhotosNetworkSource
import com.example.kmmtest.network.photos.PhotosNetworkSourceImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module

val coreNetworkModule = module {
    single { provideKtorClient() }
    single<PhotosNetworkSource> { PhotosNetworkSourceImpl(get()) }
}

val photosDomainModule = module {
    factory { GetPhotosUseCase(get()) }
    factory { GetPhotoUseCase(get()) }
}

val appModules = listOf(coreNetworkModule, photosDomainModule)