package com.tobiasschuerg.photobooth

import android.app.Application
import timber.log.Timber

class PhotoboothApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

}