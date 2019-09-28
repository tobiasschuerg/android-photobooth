package com.tobiasschuerg.photobooth.ui.main

import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.tobiasschuerg.photobooth.R

class SettingsActivity : AppCompatActivity(R.layout.settings) {

    override fun onResume() {
        super.onResume()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val edit: TextInputEditText = findViewById(R.id.raspberry_url_edit)

        val initialUrl = prefs.getString(PREFS_KEY_RASPBERRY_URL, RASPBERRY_URL_DEFAULT)
        edit.setText(initialUrl)

        edit.doAfterTextChanged {
            val newUrl = it?.toString()
            prefs.edit().putString(PREFS_KEY_RASPBERRY_URL, newUrl).apply()
        }


    }

    companion object {
        val PREFS_KEY_RASPBERRY_URL = "raspberry_url"
        val RASPBERRY_URL_DEFAULT = "http://192.168.178.11:8000"
    }

}