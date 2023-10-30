package com.unsplash.shared.di

import com.unsplash.shared.presentation.photos.details.PhotoDetailsViewModel
import com.unsplash.shared.presentation.photos.list.PhotoListViewModel
import com.unsplash.shared.presentation.photos.search.PhotosSearchViewModel
import com.unsplash.shared.presentation.settings.SettingsViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

actual fun photosPresentationModule() = module {
    viewModel { PhotoListViewModel(get(), get()) }
    viewModel { PhotosSearchViewModel(get()) }
    viewModel { PhotoDetailsViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}