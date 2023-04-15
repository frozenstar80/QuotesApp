package com.example.quotesapp.presentation.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.R
import com.example.quotesapp.databinding.BottomSheetFragmentSignupBinding
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel
import com.example.quotesapp.presentation.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpBottomSheetFragment : BaseBottomSheetFragment<BottomSheetFragmentSignupBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetFragmentSignupBinding =
        BottomSheetFragmentSignupBinding::inflate

    private val signUpViewModel by viewModels<SignUpViewModel>()
    private val launchScreenViewModel by activityViewModels<SharedLaunchScreenViewModel>()

    override fun setup() {
        launchScreenViewModel.apply {
            isLaunchPageOpened.value = false
        }
        signUpViewModel.postLiveData.observe(this){
            if (it.status==false) toast(it.message)
            else{
                toast(resources.getString(R.string.register_sucess))
                registrationSuccessful()
            }
        }
        binding?.btnRegisterUser?.setOnClickListener {
            if (validation()) {
                val map = HashMap<String, String>()
                map.put("email", binding?.edtEmail?.text.toString().trim())
                map.put("password", binding?.edtPassword?.text.toString().trim())
                signUpViewModel.getMemes(map)
            }
            }
        binding?.btnLogin?.setOnClickListener {
            findNavController().navigate(SignUpBottomSheetFragmentDirections.actionSignUpBottomSheetFragmentToLoginBottomSheetFragment())
        }
    }

    private fun registrationSuccessful() {
        findNavController().navigate(SignUpBottomSheetFragmentDirections.actionSignUpBottomSheetFragmentToProfileFragment())
    }
    private fun validation():Boolean{
        if (binding?.edtEmail?.text.toString().trim().isEmpty()){
            toast(resources.getString(R.string.enter_email_id))
            return false
        }
        if (binding?.edtPassword?.text.toString().trim().isEmpty()){
            toast(resources.getString(R.string.enter_pass_toast))
            return false
        }
        if (binding?.edtPassword?.text.toString().trim() != binding?.edtConfPassword?.text.toString().trim()){
            toast(resources.getString(R.string.confirm_pass_toast))
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        launchScreenViewModel.isLaunchPageOpened.value = true
    }
}
