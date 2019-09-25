package com.tobiasschuerg.photobooth.collage

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import timber.log.Timber


class CollageServiceImpl(private val border: Int = 100) : CollageService {

    override fun create(bitmaps: List<Bitmap>): Bitmap {
        // assume same size

        val photo1 = bitmaps[0]
        val photo2 = bitmaps[1]
        val photo3 = bitmaps[2]
        val photo4 = bitmaps[3]

        val photoWidth = photo1.width
        val photoHeight = photo1.height

        val width = 2 * photoWidth + 3 * border
        val height = 2 * photoHeight + 3 * border
        Timber.d("Creating bitmap with height $height and width $width")
        val comboBitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val comboImage = Canvas(comboBitmap)
        comboImage.drawColor(Color.WHITE)

        comboImage.drawBitmap(photo1, border.toFloat(), border.toFloat(), null)
        comboImage.drawBitmap(photo2, photoWidth.toFloat() + 2 * border, border.toFloat(), null)
        comboImage.drawBitmap(photo3, border.toFloat(), photoHeight + 2 * border.toFloat(), null)
        comboImage.drawBitmap(
            photo4,
            photoWidth.toFloat() + 2 * border,
            photoHeight.toFloat() + 2 * border,
            null
        )

        return comboBitmap
    }

}