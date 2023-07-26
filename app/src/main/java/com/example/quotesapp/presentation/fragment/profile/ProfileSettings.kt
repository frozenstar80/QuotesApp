package com.example.quotesapp.presentation.fragment.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentDisplayBinding
import com.example.quotesapp.databinding.FragmentSettingsBinding
import com.example.quotesapp.presentation.activities.MainActivity
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettings : BaseFragment<FragmentSettingsBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding =
        FragmentSettingsBinding::inflate

    override fun setup() {
        binding?.logout?.setOnClickListener {
            savedPrefManager.putLogin(false)
            savedPrefManager.putLoginDetails("","","","")
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
        binding?.txtTitle?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
            val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
            view.visibility = View.GONE
    }
}