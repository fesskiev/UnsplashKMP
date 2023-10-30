package com.unsplash.shared.di

import androidx.work.WorkManager
import com.unsplash.shared.data.utils.download.PhotoDownloadManager
import com.unsplash.shared.data.utils.download.PhotoDownloadManageImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun photoDownloadManagerModule() = module {
    single { WorkManager.getInstance(androidContext()) }
    single<PhotoDownloadManager> { PhotoDownloadManageImpl(get()) }
}