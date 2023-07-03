package com.example.kmmtest.di

import org.koin.core.context.startKoin

    fun initKoin() {
        startKoin {
            modules(appModules)
        }
    }
