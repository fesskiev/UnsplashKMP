package com.unsplash.android.utils.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.unsplash.android.R
import com.unsplash.shared.data.utils.notification.AppNotificationManager
import kotlin.random.Random

class AppNotificationManagerImpl(
    private val context: Context,
    private val notificationManager: NotificationManager
) : AppNotificationManager {

    private val NOTIFICATION_ID: Int = Random.nextInt()
    private val CHANNEL_ID: String = ""
    private var notificationBuilder: NotificationCompat.Builder? = null

    override fun show(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Photo downloader channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(channel)
        }

        notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_save)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSilent(true)


        notificationBuilder?.let {
            println("Show Notification!")
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, it.build())
        }
    }

    override fun updateProgress(progress: Int) {
        notificationBuilder?.setProgress(100, progress, false)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder?.build())
    }

    override fun remove() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}