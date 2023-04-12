package com.example.quotesapp.presentation.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quotesapp.databinding.FragmentLaunchScreenBinding


@SuppressLint("CustomSplashScreen")
class HomeFragment : BaseFragment<FragmentLaunchScreenBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLaunchScreenBinding =
        FragmentLaunchScreenBinding::inflate

    override fun setup() {

    }
}