package com.uchi.sling.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uchi.sling.BuildConfig
import timber.log.Timber
import timber.log.Timber.Forest.plant

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
        finish()
    }
}