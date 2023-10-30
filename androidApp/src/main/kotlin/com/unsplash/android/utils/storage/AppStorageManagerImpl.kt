package com.unsplash.android.utils.storage

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.unsplash.shared.data.utils.storage.AppStorageManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

class AppStorageManagerImpl(
    private val context: Context
) : AppStorageManager {

    override fun savePhotoToFile(byteArray: ByteArray) {
        val fileName = UUID.randomUUID().toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveToScopedStorage(byteArray, fileName)
        } else {
            saveToInternalStorage(byteArray, fileName)
        }
    }

    private fun saveToScopedStorage(byteArray: ByteArray, fileName: String) {
        try {
            with(context.contentResolver) {
                val imageCollection =
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                val imageDetails = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                insert(imageCollection, imageDetails)?.let { imageUri ->
                    openOutputStream(imageUri)?.use { outputStream ->
                        outputStream.write(byteArray)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveToInternalStorage(byteArray: ByteArray, fileName: String) {
        try {
            val directory = File(context.filesDir, "images")
            if (!directory.exists()) {
                directory.mkdir()
            }
            val file = File(directory, fileName)
            FileOutputStream(file).use { outputStream ->
                outputStream.write(byteArray)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}