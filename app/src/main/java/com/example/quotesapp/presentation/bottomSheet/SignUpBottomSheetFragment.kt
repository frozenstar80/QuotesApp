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
    val map = HashMap<String, String>()

    override fun setup() {
        launchScreenViewModel.apply {
            isLaunchPageOpened.value = false
        }
        signUpViewModel.postLiveData.observe(this){
            if (it.status==false) toast(it.message)
            else{
                map.put("email", binding?.edtEmail?.text.toString().trim())
                map.put("password", binding?.edtPassword?.text.toString().trim())
                signUpViewModel.loginUser(map)
            }
        }
        signUpViewModel.loginLiveData.observe(this){
            if (it.status==false) toast(it.message)
            else{
                val photo = if (it.data?.user?.photos?.isEmpty() == true) "" else it.data?.user?.photos?.get(0)
                savedPrefManager.putLoginDetails(it?.data?.token,it?.data?.user?.fullName,photo,it?.data?.user?.Id)
                registrationSuccessful()
            }
        }
        binding?.btnRegisterUser?.setOnClickListener {
            if (validation()) {
                val map = HashMap<String, String>()
                map.put("email", binding?.edtEmail?.text.toString().trim())
                map.put("password", binding?.edtPassword?.text.toString().trim())
                signUpViewModel.signInUser(map)
            }
            }
        binding?.btnLogin?.setOnClickListener {
            findNavController().navigate(SignUpBottomSheetFragmentDirections.actionSignUpBottomSheetFragmentToLoginBottomSheetFragment())
        }
    }

    private fun registrationSuccessful() {
        savedPrefManager.putLogin(true)
        toast(resources.getString(R.string.register_sucess))
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
