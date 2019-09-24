package com.tobiasschuerg.photobooth.collage

import android.graphics.Bitmap

interface CollageService {

    fun create(bitmaps: List<Bitmap>): Bitmap

}