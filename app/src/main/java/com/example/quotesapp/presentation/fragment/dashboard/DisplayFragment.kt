package com.example.quotesapp.presentation.fragment.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentDisplayBinding
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayFragment : BaseFragment<FragmentDisplayBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDisplayBinding =
        FragmentDisplayBinding::inflate

    private val args by navArgs<DisplayFragmentArgs>()

    override fun setup() {
    when(args.type){
      Constants.IMAGE,Constants.QUOTE->{
          binding?.imageView?.isVisible = true
          if (args.isExchnage) GlideLoader(requireContext()).loadUserPicture("http://143.110.247.128:8008/exchange/image/"+args.name,binding!!.imageView)
          else GlideLoader(requireContext()).loadPicFromWeb(args.name,binding!!.imageView)
      }
  }
        binding?.close?.setOnClickListener {
                requireActivity().onBackPressed()
        }



    }

    override fun onResume() {
        super.onResume()
            val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
            view.visibility = View.GONE
    }
}