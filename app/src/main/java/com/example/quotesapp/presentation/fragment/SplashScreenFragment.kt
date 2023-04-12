package com.example.quotesapp.presentation.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashScreenBinding =
        FragmentSplashScreenBinding::inflate

    override fun setup() {

        val SPLASH_SCREEN_TIME_OUT : Long = 2000

        Handler().postDelayed(Runnable {
            SplashScreenFragmentDirections.actionSplashScreenFragmentToLaunchScreenFragment()
        }, SPLASH_SCREEN_TIME_OUT)

    }
}