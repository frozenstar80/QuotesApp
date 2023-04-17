package com.example.quotesapp.presentation.activities

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quotesapp.R
import com.example.quotesapp.util.SavedPrefManager
import com.example.quotesapp.util.locale
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    lateinit var savedPrefManager: SavedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedPrefManager = SavedPrefManager(this)
        locale(savedPrefManager.getLocale().toString())
        setContentView(R.layout.activity_splash)
        val window = window

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this,R.color.black)

        val SPLASH_SCREEN_TIME_OUT : Long = 3000

        Handler().postDelayed({
            if (savedPrefManager.isLogin())
                startActivity(Intent(this, HomeActivity::class.java))
            else
                startActivity(Intent(this,MainActivity::class.java))
            finish()

        }, SPLASH_SCREEN_TIME_OUT)
    }


}