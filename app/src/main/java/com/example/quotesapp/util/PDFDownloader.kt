package com.example.quotesapp.util

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.quotesapp.util.Constants.WRITE_PDF_REQUEST_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class PDFDownloader(val context : Context,val activity: Activity,val endPoint:String) {

    fun checkPermission(){
        val permission = WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), WRITE_PDF_REQUEST_CODE)
        } else {
            downloadPDF()
        }
    }


     fun downloadPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadPdfNewVersion()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun downloadPdfNewVersion(){

        GlobalScope.launch {
            val displayName = endPoint
            val mimeType = endPoint.split(".").run {
                if (this.isNotEmpty()) this[1] else ""
            }
            val contentResolver = context.contentResolver
            // Permission granted, download the PDF file
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, displayName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
            }
            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            if (uri == null) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to create file", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            // Write the PDF file to the new file using an OutputStream
            try {
                val outputStream = contentResolver.openOutputStream(uri)
                val pdfUrl = "http://143.110.247.128:8008/user/document/$endPoint"
                val pdfStream = withContext(Dispatchers.IO) {
                    URL(pdfUrl).openStream()
                }
                pdfStream.use { input ->
                    outputStream?.use { output ->
                        input.copyTo(output)
                    }
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "PDF downloaded successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                // Failed to write the file
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to write file", Toast.LENGTH_SHORT).show()
                }
                contentResolver.delete(uri, null, null)
                return@launch
            }
        }
    }
}