package com.tobiasschuerg.photobooth.upload

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SlideshowBackend {

    @Multipart
    @POST("upload")
    fun upload(@Part files: MultipartBody.Part, @Part("print") print: Boolean): Call<String>

}