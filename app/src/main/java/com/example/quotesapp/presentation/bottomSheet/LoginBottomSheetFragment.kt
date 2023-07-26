package com.example.quotesapp.presentation.bottomSheet

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.R
import com.example.quotesapp.databinding.BottomSheetFragmentLoginBinding
import com.example.quotesapp.presentation.activities.HomeActivity
import com.example.quotesapp.presentation.viewModel.LoginViewModel
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel
import com.google.android.material.snackbar.Snackbar
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
//        binding?.edtPassword?.setText("12345678")


           loginViewModel.postLiveData.observe(this){
               if (it.status==false) showSnackBar(it.message)
               else{
                   val photo = if (it.data?.user?.photos?.isEmpty() == true) "" else it.data?.user?.photos?.get(0)
                   savedPrefManager.putLoginDetails(it?.data?.token,it?.data?.user?.fullName,photo,it?.data?.user?.Id)
                   savedPrefManager.putEmail(binding?.edtEmail?.text.toString().trim())
                   savedPrefManager.putLogin(true)
                   showSnackBar(resources.getString(R.string.login_sucess))
                   if (it?.data?.user?.fullName?.isNotEmpty() == true) {
                       startActivity(Intent(requireActivity(), HomeActivity::class.java))
                       requireActivity().finish()
                   }
                   else{
                       findNavController().navigate(LoginBottomSheetFragmentDirections.actionLoginBottomSheetFragmentToProfileFragment())
                   }
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
        if (binding?.edtEmail?.text.toString().trim().isEmpty() || binding?.edtPassword?.text.toString().trim().isEmpty()){
            showSnackBar(resources.getString(R.string.email_address_or_password_is_missing))
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