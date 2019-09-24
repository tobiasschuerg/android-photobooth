package com.tobiasschuerg.photobooth.collage

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import timber.log.Timber


class CollageServiceImpl(private val border: Int = 100) : CollageService {

    override fun create(bitmaps: List<Bitmap>): Bitmap {
        // assume same size

        val photo1 = bitmaps.first()
        val photo2 = bitmaps[1]

        val width = photo1.width + photo2.width + 3 * border
        val height = photo1.height + 2 * border
        Timber.d("Creating bitmap with height $height and width $width")
        val comboBitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val comboImage = Canvas(comboBitmap)
        comboImage.drawColor(Color.WHITE)

        comboImage.drawBitmap(photo1, border.toFloat(), border.toFloat(), null)
        comboImage.drawBitmap(photo2, photo1.width.toFloat() + 2 * border, border.toFloat(), null)

        return comboBitmap
    }

}