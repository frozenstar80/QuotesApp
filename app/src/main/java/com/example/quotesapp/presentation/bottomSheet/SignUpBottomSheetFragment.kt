package com.example.quotesapp.presentation.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quotesapp.databinding.BottomSheetFragmentLoginBinding
import com.example.quotesapp.databinding.BottomSheetFragmentSignupBinding


class SignUpBottomSheetFragment : BaseBottomSheetFragment<BottomSheetFragmentSignupBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetFragmentSignupBinding =
        BottomSheetFragmentSignupBinding::inflate

    override fun setup() {
        registrationSuccessful()
        binding?.btnRegisterUser?.setOnClickListener {

        }
        binding?.btnLogin?.setOnClickListener {
            SignUpBottomSheetFragmentDirections.actionSignUpBottomSheetFragmentToLoginBottomSheetFragment()
            dismiss()
        }
    }

    private fun registrationSuccessful() {
    }}
