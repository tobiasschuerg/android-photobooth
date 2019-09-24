package com.tobiasschuerg.photobooth.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.tobiasschuerg.photobooth.R
import com.tobiasschuerg.photobooth.base.BaseFragment
import com.tobiasschuerg.photobooth.collage.CollageServiceImpl
import com.tobiasschuerg.photobooth.gphoto.GPhoto2ServiceImpl
import com.tobiasschuerg.photobooth.util.FullscreenUtil.hideSystemUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class MainFragment : BaseFragment(R.layout.main_fragment) {

    private lateinit var infoText: TextView
    private lateinit var message: TextView

    private lateinit var preview1: ImageView
    private lateinit var preview2: ImageView
    private lateinit var preview3: ImageView
    private lateinit var preview4: ImageView

    private lateinit var button: Button

    private var session = 0
    private var photos: List<Uri> = mutableListOf()

    private val photoService = GPhoto2ServiceImpl()

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        infoText = view.findViewById(R.id.filename)
        message = view.findViewById(R.id.message)

        preview1 = view.findViewById(R.id.preview1)
        preview2 = view.findViewById(R.id.preview2)
        preview3 = view.findViewById(R.id.preview3)
        preview4 = view.findViewById(R.id.preview4)

        button = view.findViewById<Button>(R.id.take_a_photo_button)
        button.setOnClickListener {
            hideSystemUI(activity!!.window)
            launch {
                newSession()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val jan: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.jan)
        val sa: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.sa)
        val tp: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.pt)

        val imput = listOf(jan, sa, tp)

        Glide.with(this).load(imput.random()).into(preview2)

        val cs = CollageServiceImpl()
        Timber.d("bitmaps loaded")
        val collage: Bitmap = cs.create(imput)
        Timber.d("bitmap created")

        Glide.with(this).load(collage).into(preview3)
    }

    private suspend fun newSession() {
        button.visibility = View.GONE
        session++
        photoService.status()

        Timber.i("Take photo")
        infoText.text = getString(R.string.get_ready_)
        delay(2500)

        (0..2).forEach {
            try {
                launch {
                    countDown(5)
                }
                // there is a delay for the camera to respond
                delay(3000)

                val fileName = photoService.capture()
                message.text = fileName

                val thumb = photoService.thumbnail(fileName)

                val view = when (it % 4) {
                    0 -> preview1
                    1 -> preview2
                    2 -> preview3
                    3 -> preview4
                    else -> throw IllegalArgumentException()
                }
                Glide.with(this).load(thumb).into(view)

                // TODO: download full file


            } catch (e: Exception) {
                Timber.e(e, "take photo failed")
                showMessage("Capturing photo failed")
            }
        }
        button.visibility = View.VISIBLE
    }

    private suspend fun countDown(seconds: Int = 5) {
        (seconds downTo 1).forEach {
            infoText.text = it.toString()
            delay(1000)
        }
        infoText.text = "⇧"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
