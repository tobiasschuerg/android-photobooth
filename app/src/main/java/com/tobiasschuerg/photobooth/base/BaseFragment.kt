package com.tobiasschuerg.photobooth.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout), CoroutineScope {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val fragment = javaClass.simpleName
        Timber.e(exception, "in $fragment")
        throw exception
    }

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + exceptionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun showMessage(message: String) {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(message)
                .show()
        }
    }
}