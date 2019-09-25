package com.tobiasschuerg.photobooth.upload

import android.net.Uri
import androidx.core.net.toFile
import com.tobiasschuerg.photobooth.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber


object Uploader {

    class BasicAuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", Config.SLIDESHOW_CREDENTIALS)
                .build()
            return chain.proceed(authenticatedRequest)
        }

    }

    private val api: SlideshowBackend by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Config.SLIDESHOW_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        retrofit.create(SlideshowBackend::class.java)
    }

    suspend fun upload(uri: Uri) = withContext(Dispatchers.IO) {
        Timber.d("Upload $uri")
        val file = uri.toFile()
        val mediaType: MediaType =
            "multipart/form-data".toMediaTypeOrNull() ?: throw IllegalStateException()
        val requestFile = file.asRequestBody(mediaType)

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("files", file.name, requestFile)

        try {
            val response: Response<String> = api.upload(body).awaitResponse()
            Timber.i("Upload finished: ${response.body()}")
        } catch (t: Throwable) {
            Timber.e(t, "Upload failed")
        }
    }

}