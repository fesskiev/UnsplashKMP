package com.example.kmmtest.android

import android.app.Application
import com.example.kmmtest.di.initKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
