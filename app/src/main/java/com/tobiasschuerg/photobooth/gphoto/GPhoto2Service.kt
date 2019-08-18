package com.tobiasschuerg.photobooth.gphoto

interface GPhoto2Service {

    suspend fun status(): String

    suspend fun capture(): String

}