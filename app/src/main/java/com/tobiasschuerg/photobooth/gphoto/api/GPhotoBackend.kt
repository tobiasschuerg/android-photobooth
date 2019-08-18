package com.tobiasschuerg.photobooth.gphoto.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GPhotoBackend {

    @GET("info")
    fun info(): Call<String>

}