package com.unsplash.shared.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun settingsModule() = module {
    single {
        NSUserDefaultsSettings(
            NSUserDefaults()
        ).toFlowSettings()
    }
}