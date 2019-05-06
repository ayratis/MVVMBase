package com.ayratis.abstractapp.ui.auth._activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ayratis.abstractapp.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
        setContentView(R.layout.activity_auth)
    }
}
