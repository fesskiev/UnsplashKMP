package com.unsplash.shared.data.utils.notification

interface AppNotificationManager {

    fun show(title: String, message: String)

    fun updateProgress(progress: Int)

    fun remove()
}