package com.tobiasschuerg.photobooth.gphoto

import android.graphics.Bitmap

interface GPhoto2Service {

    suspend fun status(): String

    suspend fun capture(): String

    suspend fun thumbnail(fileName: String): Bitmap
}