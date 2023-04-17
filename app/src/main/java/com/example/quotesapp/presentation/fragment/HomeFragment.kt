package com.example.quotesapp.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quotesapp.databinding.FragmentHomeBinding
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    override fun setup() {
        binding?.txtSearchView?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToSearchPeopleFragment())
        }
        GlideLoader(requireContext()).loadUserPicture("${Constants.IMAGE_BASE_URL}${savedPrefManager.getPhoto()}",binding?.imgProfilePic)
        binding?.txtUserName?.text = savedPrefManager.getFullName()
    }
}