package com.example.quotesapp.presentation.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.quotesapp.databinding.FragmentLaunchScreenBinding
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel


@SuppressLint("CustomSplashScreen")
class LaunchScreenFragment : BaseFragment<FragmentLaunchScreenBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLaunchScreenBinding =
        FragmentLaunchScreenBinding::inflate

    private val launchScreenViewModel by activityViewModels<SharedLaunchScreenViewModel>()

    override fun setup() {

        LaunchScreenFragmentDirections.actionLaunchScreenFragmentToLanguageChooserBottomSheetFragment()

        launchScreenViewModel.apply {
            isLaunchPageOpened.observe(this@LaunchScreenFragment) {
               binding?.txtQuote?.isVisible = it
                binding?.txtViewAllQuote?.isVisible = it
            }
        }

    }
}