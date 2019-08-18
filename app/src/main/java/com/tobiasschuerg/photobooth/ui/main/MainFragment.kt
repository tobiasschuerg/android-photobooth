package com.tobiasschuerg.photobooth.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tobiasschuerg.photobooth.FullscreenUtil.hideSystemUI
import com.tobiasschuerg.photobooth.R
import com.tobiasschuerg.photobooth.gphoto.GPhoto2ServiceImpl
import com.tobiasschuerg.photobooth.gphoto.base.BaseFragment
import kotlinx.coroutines.launch

class MainFragment : BaseFragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.take_a_photo_button)
        button.setOnClickListener { hideSystemUI(activity!!.window)
            launch{
                foo()
            }
        }
    }

    private suspend fun foo() {
        val photoServiceLifecycleDispatcher =  GPhoto2ServiceImpl()
        photoServiceLifecycleDispatcher.status()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
