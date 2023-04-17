package com.example.quotesapp.presentation.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.databinding.FragmentLaunchScreenBinding
import com.example.quotesapp.presentation.activities.MainActivity
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel
import com.example.quotesapp.util.locale
import java.util.*


@SuppressLint("CustomSplashScreen")
class LaunchScreenFragment : BaseFragment<FragmentLaunchScreenBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLaunchScreenBinding =
        FragmentLaunchScreenBinding::inflate

    private val launchScreenViewModel by activityViewModels<SharedLaunchScreenViewModel>()

    override fun setup() {

        launchScreenViewModel.apply {
            isLaunchPageOpened.observe(this@LaunchScreenFragment) {
               binding?.txtQuote?.isVisible = it
                binding?.txtViewAllQuote?.isVisible = it
            }
        }
        binding?.btnLogin?.setOnClickListener {
            findNavController().navigate(LaunchScreenFragmentDirections.actionLaunchScreenFragmentToLoginBottomSheetFragment())
        }
        binding?.btnRegister?.setOnClickListener {
            findNavController().navigate(LaunchScreenFragmentDirections.actionLaunchScreenFragmentToSignUpBottomSheetFragment())
        }
        binding?.radEnglish?.setOnClickListener {
            binding?.radEnglish?.isChecked = true
            binding?.radGerman?.isChecked = false
            callRefreshText("en")
        }
        binding?.radGerman?.setOnClickListener {
            binding?.radGerman?.isChecked = true
            binding?.radEnglish?.isChecked = false
            callRefreshText("de")
        }


    }

    private fun callRefreshText(lang:String){
        savedPrefManager.putLocale(lang)
        locale(lang)
        requireActivity().recreate()
    }


}