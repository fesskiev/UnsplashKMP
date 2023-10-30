package com.unsplash.shared.data.utils

suspend fun <T> dataSourceStrategy(
    getLocalData: suspend () -> List<T>,
    getRemoteData: suspend () -> List<T>,
    saveLocalData: suspend (List<T>) -> Unit,
): Result<List<T>> {
    val localData = getResult { getLocalData() }
    return if (localData is Result.Success && localData.data.isNotEmpty()) {
        localData
    } else {
        val remoteData = getResult { getRemoteData() }
        if (remoteData is Result.Success && remoteData.data.isNotEmpty()) {
            saveLocalData(remoteData.data)
        }
        remoteData
    }
}
