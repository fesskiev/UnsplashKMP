package com.unsplash.shared.di

import com.unsplash.shared.data.utils.download.PhotoDownloadManager
import com.unsplash.shared.data.utils.download.PhotoDownloadManagerImpl
import org.koin.dsl.module

actual fun photoDownloadManagerModule() = module {
    single<PhotoDownloadManager> { PhotoDownloadManagerImpl() }
}