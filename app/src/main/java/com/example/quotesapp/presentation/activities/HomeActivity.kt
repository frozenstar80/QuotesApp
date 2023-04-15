package com.example.quotesapp.presentation.activities

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.quotesapp.R
import com.example.quotesapp.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale("de")
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_home_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

    }

    private fun setLocale(lang: String?) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources: Resources = getResources()
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}