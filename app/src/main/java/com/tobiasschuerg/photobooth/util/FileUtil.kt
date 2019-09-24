package com.tobiasschuerg.photobooth.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


object FileUtil {

    suspend fun saveToFile(bitmap: Bitmap, fileName: String, context: Context): Uri =
        withContext(Dispatchers.IO) {
            // val dir = context.getExternalFilesDir("photobooth")
            val dir = Environment.getExternalStoragePublicDirectory("photobooth")
            if (!dir.exists()) dir.mkdir()
            // val path = getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
            val file = File(dir, "${UUID.randomUUID()}.jpg")

            try {
                // Compress the bitmap and save in jpg format
                val stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream)
                stream.flush()
                stream.close()
            } catch (e: IOException) {
                Timber.e(e, "Writing file failed")
            }

            file.toUri()
        }

}