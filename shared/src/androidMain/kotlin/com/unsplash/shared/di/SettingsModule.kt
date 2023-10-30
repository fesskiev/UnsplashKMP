package com.unsplash.shared.di

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual fun settingsModule() = module {
    single { androidApplication().getSharedPreferences("Settings", Context.MODE_PRIVATE) }
    single {
        SharedPreferencesSettings(
            get(),
            commit = false
        ).toFlowSettings(dispatcher = Dispatchers.IO)
    }
}