package com.unsplash.shared.di

import com.unsplash.shared.presentation.photos.details.PhotoDetailsViewModel
import com.unsplash.shared.presentation.photos.list.PhotoListViewModel
import com.unsplash.shared.presentation.photos.search.PhotosSearchViewModel
import com.unsplash.shared.presentation.settings.SettingsViewModel
import org.koin.dsl.module

actual fun photosPresentationModule() = module {
    factory { PhotoListViewModel(get(), get()) }
    factory { PhotosSearchViewModel(get()) }
    factory { PhotoDetailsViewModel(get(), get()) }
    factory { SettingsViewModel(get()) }
}