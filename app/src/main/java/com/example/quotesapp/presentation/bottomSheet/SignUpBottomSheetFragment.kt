package com.example.quotesapp.presentation.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.R
import com.example.quotesapp.databinding.BottomSheetFragmentSignupBinding
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel
import com.example.quotesapp.presentation.viewModel.SignUpViewModel
import com.google.android.material.snackbar.Snackbar
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
//        binding?.edtPassword?.setText("12345678")
//        binding?.edtConfPassword?.setText("12345678")


        signUpViewModel.postLiveData.observe(this){
            if (it.status==false) showSnackBar(it.message)
            else{
                map.put("email", binding?.edtEmail?.text.toString().trim())
                map.put("password", binding?.edtPassword?.text.toString().trim())
                signUpViewModel.loginUser(map)
            }
        }
        signUpViewModel.loginLiveData.observe(this){
            if (it.status==false) showSnackBar(it.message)
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
        savedPrefManager.putEmail(binding?.edtEmail?.text.toString().trim())
        showSnackBar(resources.getString(R.string.register_sucess))
        findNavController().navigate(SignUpBottomSheetFragmentDirections.actionSignUpBottomSheetFragmentToProfileFragment())
    }
    private fun validation():Boolean{
        if (binding?.edtEmail?.text.toString().trim().isEmpty() || binding?.edtPassword?.text.toString().trim().isEmpty()){
            showSnackBar(resources.getString(R.string.email_address_or_password_is_missing))
            return false
        }
        if (binding?.edtPassword?.text.toString().trim() != binding?.edtConfPassword?.text.toString().trim()){
            showSnackBar(resources.getString(R.string.confirm_pass_toast))
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        launchScreenViewModel.isLaunchPageOpened.value = true
    }

    private fun showSnackBar(message: String?) {
        val view = view?.findViewById<CoordinatorLayout>(R.id.lyt_root)
        val snackBar =
            view?.let { Snackbar.make(it, message?: "", Snackbar.LENGTH_LONG) }
         val snackBarView = snackBar?.view

            snackBarView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        snackBar?.show()
    }



}
