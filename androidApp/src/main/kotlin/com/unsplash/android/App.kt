package com.unsplash.android

import android.app.Application
import com.unsplash.android.di.uiModule
import com.unsplash.shared.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModules + uiModule)
        }
    }
}
