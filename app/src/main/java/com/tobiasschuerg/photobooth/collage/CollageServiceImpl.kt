package com.tobiasschuerg.photobooth.collage

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import timber.log.Timber
import kotlin.math.roundToInt


class CollageServiceImpl(private val border: Int = 64) : CollageService {

    override fun create(bitmaps: List<Bitmap>): Bitmap {
        // assume same size
        val photoWidth = bitmaps.first().width
        val photoHeight = bitmaps.first().height

        // Resize to  5 Mpix => 2.560 x 1.920
        val targetWidth = 2560 / 2
        val percentage = targetWidth / photoWidth.toFloat()
        val targetHeight = (photoHeight * percentage).roundToInt()
        Timber.d("Initial width $photoWidth, target width $targetWidth")
        Timber.d("Shrink percentage $percentage")

        val border = targetWidth / 52

        val scaled =
            bitmaps.map {
                Bitmap.createScaledBitmap(
                    it,
                    targetWidth,
                    targetHeight,
                    false
                )
            }

        val photo1 = scaled[0]
        val photo2 = scaled[1]
        val photo3 = scaled[2]
        val photo4 = scaled[3]


        val width = 2 * targetWidth + 3 * border
        val height = 2 * targetHeight + 3 * border
        Timber.d("Creating bitmap with height $height and width $width")
        val comboBitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val comboImage = Canvas(comboBitmap)
        comboImage.drawColor(Color.WHITE)

        comboImage.drawBitmap(photo1, border.toFloat(), border.toFloat(), null)
        comboImage.drawBitmap(photo2, targetWidth.toFloat() + 2 * border, border.toFloat(), null)
        comboImage.drawBitmap(photo3, border.toFloat(), targetHeight + 2 * border.toFloat(), null)
        comboImage.drawBitmap(
            photo4,
            targetWidth.toFloat() + 2 * border,
            targetHeight.toFloat() + 2 * border,
            null
        )

        return comboBitmap
    }

}