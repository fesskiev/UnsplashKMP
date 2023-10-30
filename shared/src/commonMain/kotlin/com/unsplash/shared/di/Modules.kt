package com.unsplash.shared.di

import com.unsplash.shared.domain.usecases.GetPhotoUseCase
import com.unsplash.shared.domain.usecases.GetPhotosUseCase
import com.unsplash.shared.data.remote.network.client.HttpClient
import com.unsplash.shared.data.remote.PhotosNetworkSource
import com.unsplash.shared.data.remote.PhotosNetworkSourceImpl
import com.unsplash.shared.domain.usecases.RefreshPhotosUseCase
import com.unsplash.shared.domain.usecases.SearchPhotosUseCase
import com.unsplash.shared.data.utils.settings.AppSettings
import com.unsplash.shared.data.utils.settings.AppSettingsImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun photosPresentationModule(): Module

expect fun settingsModule(): Module

expect fun photoDownloadManagerModule(): Module

val coreNetworkModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single { Dispatchers.IO }
    single { HttpClient().httpClient }
    single<PhotosNetworkSource> { PhotosNetworkSourceImpl(get(), get()) }
}

val settingsModule = module {
    single<AppSettings> { AppSettingsImpl(get(), get()) }
}

val photosDomainModule = module {
    factory { GetPhotosUseCase(get()) }
    factory { GetPhotoUseCase(get()) }
    factory { RefreshPhotosUseCase(get()) }
    factory { SearchPhotosUseCase(get()) }
}

val appModules = listOf(
    coreNetworkModule,
    settingsModule,
    photosDomainModule,
    settingsModule(),
    photosPresentationModule(),
    photoDownloadManagerModule()
)