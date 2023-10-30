package com.unsplash.android.di

import android.app.NotificationManager
import android.content.Context
import com.unsplash.shared.data.utils.notification.AppNotificationManager
import com.unsplash.android.utils.notification.AppNotificationManagerImpl
import com.unsplash.android.utils.storage.AppStorageManagerImpl
import com.unsplash.shared.data.utils.storage.AppStorageManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val uiModule = module {
    single { androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager? }
    single<AppNotificationManager> { AppNotificationManagerImpl(androidContext(), get()) }
    single<AppStorageManager> { AppStorageManagerImpl(androidContext()) }
}
