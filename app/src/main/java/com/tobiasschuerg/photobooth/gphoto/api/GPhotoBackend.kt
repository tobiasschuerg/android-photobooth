package com.tobiasschuerg.photobooth.gphoto.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GPhotoBackend {

    @GET("info")
    fun info(): Call<String>

    @GET("capture")
    fun capture(): Call<String>

    @GET("thumb/{id}")
    fun thumbnail(@Path("id") id: String): Call<ResponseBody>

    @GET("photo/{id}")
    fun fullSize(@Path("id") id: String): Call<ResponseBody>
}