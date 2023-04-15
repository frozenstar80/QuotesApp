package com.example.quotesapp.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quotesapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

}