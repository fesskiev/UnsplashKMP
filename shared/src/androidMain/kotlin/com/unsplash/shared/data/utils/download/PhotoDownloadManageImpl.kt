package com.unsplash.shared.data.utils.download

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlin.coroutines.resume
import com.unsplash.shared.data.utils.Result
import kotlin.coroutines.suspendCoroutine

internal const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
internal const val KEY_IMAGE_TITLE = "KEY_IMAGE_TITLE"
internal const val KEY_IMAGE_MESSAGE = "KEY_IMAGE_MESSAGE"

class PhotoDownloadManageImpl(
    private val workManager: WorkManager
) : PhotoDownloadManager {

    override suspend fun downloadPhoto(url: String, title: String?, message: String?): Result<Unit> =
        suspendCoroutine { continuation ->
            val downloadWorker = OneTimeWorkRequestBuilder<PhotoDownloadWorker>()
                .apply {
                    setInputData(
                        workDataOf(
                            KEY_IMAGE_URI to url,
                            KEY_IMAGE_TITLE to title,
                            KEY_IMAGE_MESSAGE to message
                        )
                    )
                }
                .build()
            workManager.enqueue(downloadWorker)
            workManager.getWorkInfoByIdLiveData(downloadWorker.id)
                .observeForever { workInfo ->
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            continuation.resume(Result.Success(Unit))
                            workManager.getWorkInfoByIdLiveData(downloadWorker.id)
                                .removeObserver { }
                        }

                        WorkInfo.State.FAILED -> {
                            continuation.resume(Result.Error(PhotoDownloadException()))
                            workManager.getWorkInfoByIdLiveData(downloadWorker.id)
                                .removeObserver { }
                        }
                        else -> {}
                    }
                }
        }
}