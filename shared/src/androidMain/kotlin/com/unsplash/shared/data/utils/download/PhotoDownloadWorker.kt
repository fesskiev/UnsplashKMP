package com.unsplash.shared.data.utils.download

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.unsplash.shared.data.remote.PhotosNetworkSource
import com.unsplash.shared.data.utils.notification.AppNotificationManager
import com.unsplash.shared.data.utils.storage.AppStorageManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhotoDownloadWorker(
    context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(context, params), KoinComponent {

    private val networkSource: PhotosNetworkSource by inject()
    private val notificationManager: AppNotificationManager by inject()
    private val storageManager: AppStorageManager by inject()

    override suspend fun doWork(): Result = try {
        val url = inputData.getString(KEY_IMAGE_URI)
        val title = inputData.getString(KEY_IMAGE_TITLE) ?: ""
        val message = inputData.getString(KEY_IMAGE_MESSAGE) ?: ""
        if (url.isNullOrEmpty()) {
            Result.failure()
        } else {
            notificationManager.show(title, message)
            networkSource.downloadPhoto(url).collect {
                val byteArray = it.first
                val progress = it.second
                notificationManager.updateProgress(progress)
                if (byteArray != null && progress == 100) {
                    storageManager.savePhotoToFile(byteArray)
                }
            }
        }
        Result.success()
    } catch (e: Exception) {
        notificationManager.remove()
        Result.failure()
    }
}