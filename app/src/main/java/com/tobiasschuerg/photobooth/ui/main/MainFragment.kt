package com.tobiasschuerg.photobooth.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.tobiasschuerg.photobooth.FullscreenUtil.hideSystemUI
import com.tobiasschuerg.photobooth.R
import com.tobiasschuerg.photobooth.gphoto.GPhoto2ServiceImpl
import com.tobiasschuerg.photobooth.gphoto.base.BaseFragment
import kotlinx.coroutines.launch

class MainFragment : BaseFragment(R.layout.main_fragment) {

    private lateinit var infoText: TextView

    private lateinit var preview1: ImageView
    private lateinit var preview2: ImageView
    private lateinit var preview3: ImageView

    private var count = 0

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        infoText = view.findViewById(R.id.filename)
        preview1 = view.findViewById(R.id.preview1)
        preview2 = view.findViewById(R.id.preview2)
        preview3 = view.findViewById(R.id.preview3)
        val button = view.findViewById<Button>(R.id.take_a_photo_button)
        button.setOnClickListener {
            hideSystemUI(activity!!.window)
            launch {
                foo()
            }
        }
    }

    private suspend fun foo() {
        val photoService = GPhoto2ServiceImpl()
        photoService.status()
        val fileName = photoService.capture()
        infoText.setText(fileName)

        val thumb = photoService.thumbnail(fileName)

        when (count++ % 3) {
            0 -> preview1.setImageBitmap(thumb)
            1 -> preview2.setImageBitmap(thumb)
            2 -> preview3.setImageBitmap(thumb)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
