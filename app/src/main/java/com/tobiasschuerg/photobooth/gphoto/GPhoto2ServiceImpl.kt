package com.tobiasschuerg.photobooth.gphoto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tobiasschuerg.photobooth.gphoto.api.GPhotoBackend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber


class GPhoto2ServiceImpl : GPhoto2Service {

    private val api: GPhotoBackend by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
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
        val byteArray = api.thumbnail(fileName).await().byteStream().readBytes()
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return bitmap
    }
}