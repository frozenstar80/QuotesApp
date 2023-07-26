package com.example.quotesapp.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import id.zelory.compressor.Compressor
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar


object Utils {
    suspend fun getCompressImagePath(context: Context, uri: Uri) : File? {
        var fileImage:File? = null
        safeLet(context, uri) { c, u ->
            val file = getTempFile(c, u) ?: return@safeLet
            fileImage = File(Compressor.compress(c, file) {
                default(width = 300, format = Bitmap.CompressFormat.JPEG)
            }.path)
        } ?: return null
        return fileImage
    }

    inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
        return if (p1 != null && p2 != null) block(p1, p2) else null
    }

    fun getTempFile(context: Context, uri: Uri): File? {
        try {
            val destination = File(context.cacheDir, "image_picker${Calendar.getInstance().timeInMillis}${getImageExtension(uri)}")

            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor ?: return null

            val src = FileInputStream(fileDescriptor).channel
            val dst = FileOutputStream(destination).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()

            return destination
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return null
    }

    fun getImageExtension(uriImage: Uri): String {
        var extension: String? = null

        try {
            val imagePath = uriImage.path
            if (imagePath != null && imagePath.lastIndexOf(".") != -1) {
                extension = imagePath.substring(imagePath.lastIndexOf(".") + 1)
            }
        } catch (e: Exception) {
            extension = null
        }

        if (extension == null || extension.isEmpty()) {
            // default extension for matches the previous behavior of the plugin
            extension = "jpg"
        }

        return ".$extension"
    }
}