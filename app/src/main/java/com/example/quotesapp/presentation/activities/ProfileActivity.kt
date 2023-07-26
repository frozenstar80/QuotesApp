package com.example.quotesapp.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quotesapp.R
import com.example.quotesapp.util.SavedPrefManager
import com.example.quotesapp.util.locale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    lateinit var savedPrefManager: SavedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedPrefManager = SavedPrefManager(this)
        locale(savedPrefManager.getLocale().toString())
        setContentView(R.layout.activity_profile)
    }


}