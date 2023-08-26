package com.droidcon.weatherstation.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

object BitmapUtils {
    fun saveImageToStorage(context: Context, bitmap: Bitmap?, fileName: String): Uri? {
        return try {
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver, bitmap, fileName, null
            )
            Uri.parse(path)
        } catch (e: Exception) {
            null
        }
    }
}