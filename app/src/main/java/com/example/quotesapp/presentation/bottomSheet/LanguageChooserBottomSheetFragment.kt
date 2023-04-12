package com.example.quotesapp.presentation.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quotesapp.databinding.BottomSheetFragmentLoginBinding
import com.example.quotesapp.databinding.BottomSheetFragmentSelectLanguageBinding


class LanguageChooserBottomSheetFragment : BaseBottomSheetFragment<BottomSheetFragmentSelectLanguageBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetFragmentSelectLanguageBinding =
        BottomSheetFragmentSelectLanguageBinding::inflate

    override fun setup() {
              binding?.btnLogin?.setOnClickListener {
                  LanguageChooserBottomSheetFragmentDirections.actionLanguageChooserBottomSheetFragmentToLoginBottomSheetFragment()
              }
              binding?.btnRegister?.setOnClickListener {
                  LanguageChooserBottomSheetFragmentDirections.actionLanguageChooserBottomSheetFragmentToSignUpBottomSheetFragment()
              }
    }
}