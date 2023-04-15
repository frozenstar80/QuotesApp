package com.example.quotesapp.presentation.bottomSheet

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.R
import com.example.quotesapp.databinding.BottomSheetFragmentLoginBinding
import com.example.quotesapp.presentation.activities.HomeActivity
import com.example.quotesapp.presentation.viewModel.LoginViewModel
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginBottomSheetFragment : BaseBottomSheetFragment<BottomSheetFragmentLoginBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetFragmentLoginBinding =
        BottomSheetFragmentLoginBinding::inflate

    private val loginViewModel by viewModels<LoginViewModel>()
    private val launchScreenViewModel by activityViewModels<SharedLaunchScreenViewModel>()

    override fun setup() {
        launchScreenViewModel.apply {
            isLaunchPageOpened.value = false
        }

           loginViewModel.postLiveData.observe(this){
               if (it.status==false) toast(it.message)
               else{
                   savedPrefManager.putLogin(true)
                   toast(resources.getString(R.string.login_sucess))
                   startActivity(Intent(requireActivity(), HomeActivity::class.java))
                   requireActivity().finish()
                   }
               }


           binding?.btnLoginUser?.setOnClickListener {
               if (validation()){
                   val map = HashMap<String,String>()
                   map.put("email",binding?.edtEmail?.text.toString().trim())
                   map.put("password",binding?.edtPassword?.text.toString().trim())
                  loginViewModel.getMemes(map)
           }
           }

//           binding?.txtForgetPassword?.setOnClickListener {
//               findNavController().navigate(LoginBottomSheetFragmentDirections.actionLoginBottomSheetFragmentToForgetPasswordBottomSheetFragment())
//           }

          binding?.btnRegister?.setOnClickListener {
              findNavController().navigate(LoginBottomSheetFragmentDirections.actionLoginBottomSheetFragmentToSignUpBottomSheetFragment())
          }
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
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        launchScreenViewModel.isLaunchPageOpened.value = true
    }

}