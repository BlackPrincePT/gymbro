package com.pegio.gymbro.presentation.core


import android.content.Context
import android.net.Uri
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class ImageUtil @Inject constructor(@ApplicationContext private val context: Context) {
    fun encodeImageToBase64(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val bytes = inputStream.readBytes()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
