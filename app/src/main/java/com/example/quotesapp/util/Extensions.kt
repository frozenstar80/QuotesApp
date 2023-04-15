package com.example.quotesapp.util


import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import java.util.*

fun AppCompatActivity.locale(lang:String) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val resources: Resources = resources
    val config: Configuration = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}