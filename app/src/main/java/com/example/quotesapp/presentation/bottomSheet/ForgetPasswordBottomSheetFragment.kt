package com.example.quotesapp.presentation.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.databinding.BottomSheetFragmentForgetPasswordBinding
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel


class ForgetPasswordBottomSheetFragment : BaseBottomSheetFragment<BottomSheetFragmentForgetPasswordBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetFragmentForgetPasswordBinding =
        BottomSheetFragmentForgetPasswordBinding::inflate

    private val launchScreenViewModel by activityViewModels<SharedLaunchScreenViewModel>()

    override fun setup() {
              binding?.btnVerifyEmail?.setOnClickListener {
                  findNavController().navigate(ForgetPasswordBottomSheetFragmentDirections.actionForgetPasswordBottomSheetFragmentToResetPasswordBottomSheetFragment())

              }

    }


}