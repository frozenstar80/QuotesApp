package com.example.quotesapp.presentation.fragment.buriaModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.R
import com.example.quotesapp.data.BurialData
import com.example.quotesapp.databinding.FragmentBurialBinding
import com.example.quotesapp.presentation.adapter.BurialAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.BurialViewModel
import com.example.quotesapp.util.BurialItemClickListener
import com.example.quotesapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BurialFragment : BaseFragment<FragmentBurialBinding>(),BurialItemClickListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBurialBinding =
        FragmentBurialBinding::inflate

    private val viewModel by viewModels<BurialViewModel>()
    var itemType=""

    override fun setup() {
        val userType = arguments?.getString(Constants.USER_TYPE).toString()
        val id = arguments?.getString(Constants.USER_ID).toString()
        itemType = arguments?.getString(Constants.ITEM_TYPE).toString()
        val tab =  arguments?.getString(Constants.TAB).toString()
        val isOtherProfile = arguments?.getBoolean(Constants.IS_OTHER_PROFILE)

        viewModel.getBurial(id,savedPrefManager.getToken().toString(),itemType,tab)
        viewModel.getBurialLiveData.observe(viewLifecycleOwner){
            binding?.recyclerView?.adapter = BurialAdapter(requireContext(),it.data,this,isOtherProfile?.not()==true,itemType)
        }
        binding?.imgFab?.setOnClickListener {
            findNavController().navigate(BurialItemsHostFragmentDirections.actionBurialItemsHostFragmentToUploadBurialFragment(
                id = id,
                itemType = itemType,
                tab = tab,
                userType = userType
            ))
        }

    }
    override fun onResume() {
        super.onResume()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.GONE
    }

    override fun clicked(burialData: BurialData) {
        findNavController().navigate(BurialItemsHostFragmentDirections.actionBurialItemsHostFragmentToUploadBurialFragment(
            id = burialData.Id.toString(),
            itemType = itemType,
            tab = burialData.tab.toString(),
            userType = burialData.userType.toString(),
            burialData = burialData
        ))
    }

    override fun clicked(name: String) {
        findNavController().navigate(BurialItemsHostFragmentDirections.actionBurialItemsHostFragmentToDisplayFragment(type = "image", name = name, isExchnage = false))

    }

}