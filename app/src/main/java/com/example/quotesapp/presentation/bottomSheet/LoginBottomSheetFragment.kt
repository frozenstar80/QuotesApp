package com.example.quotesapp.presentation.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quotesapp.databinding.BottomSheetFragmentLoginBinding


class LoginBottomSheetFragment : BaseBottomSheetFragment<BottomSheetFragmentLoginBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetFragmentLoginBinding =
        BottomSheetFragmentLoginBinding::inflate

    override fun setup() {
           loginSuccessful()
           binding?.btnLoginUser?.setOnClickListener {

           }
          binding?.btnRegister?.setOnClickListener {
              LoginBottomSheetFragmentDirections.actionLoginBottomSheetFragmentToSignUpBottomSheetFragment()
              dismiss()
          }
    }

    private fun loginSuccessful() {
        LoginBottomSheetFragmentDirections.actionLoginBottomSheetFragmentToProfileFragment()
        dismiss()
    }
}