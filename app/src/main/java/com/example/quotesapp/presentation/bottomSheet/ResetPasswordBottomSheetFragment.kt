package com.example.quotesapp.presentation.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.databinding.BottomSheetFragmentResetPasswordBinding
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel


class ResetPasswordBottomSheetFragment : BaseBottomSheetFragment<BottomSheetFragmentResetPasswordBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetFragmentResetPasswordBinding =
        BottomSheetFragmentResetPasswordBinding::inflate

    private val launchScreenViewModel by activityViewModels<SharedLaunchScreenViewModel>()

    override fun setup() {

              binding?.btnResetPassword?.setOnClickListener {
                  findNavController().navigate(ResetPasswordBottomSheetFragmentDirections.actionResetPasswordBottomSheetFragmentToLoginBottomSheetFragment())
              }
    }


}