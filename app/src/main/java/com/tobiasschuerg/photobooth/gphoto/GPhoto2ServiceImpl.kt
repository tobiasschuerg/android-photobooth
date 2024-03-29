package com.tobiasschuerg.photobooth.gphoto

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tobiasschuerg.photobooth.gphoto.api.GPhotoBackend
import com.tobiasschuerg.photobooth.ui.main.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import kotlin.system.measureTimeMillis


class GPhoto2ServiceImpl(
    preferences: SharedPreferences
) : GPhoto2Service {

    private val raspBerryUrl: String by lazy {

        preferences.getString(
            SettingsActivity.PREFS_KEY_RASPBERRY_URL,
            SettingsActivity.RASPBERRY_URL_DEFAULT
        )!!

    }

    private val api: GPhotoBackend by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(raspBerryUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        retrofit.create(GPhotoBackend::class.java)
    }

    override suspend fun status(): String = withContext(Dispatchers.IO) {
        val infoString = api.info().await()
        Timber.d("Status response: $infoString")
        infoString
    }

    override suspend fun capture(): String = withContext(Dispatchers.IO) {
        val infoString = api.capture().await()
        Timber.d("Capture: $infoString")
        infoString
    }

    override suspend fun thumbnail(fileName: String): Bitmap {
        var bitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val millis = measureTimeMillis {
            val byteArray = api.thumbnail(fileName).await().byteStream().readBytes()
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
        Timber.i("Downloading thumbnail took $millis millis")
        return bitmap
    }

    override suspend fun fullSize(fileName: String): Bitmap {
        var bitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val millis = measureTimeMillis {
            val byteArray = api.fullSize(fileName).await().byteStream().readBytes()
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
        Timber.i("Download full size photo took $millis millis")
        return bitmap
    }
}