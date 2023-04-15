package com.example.quotesapp.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    override fun setup() {
        binding?.txtSearchView?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToSearchPeopleFragment())
        }

    }
}