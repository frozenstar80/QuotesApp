package com.example.quotesapp.presentation.fragment.exchange

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.data.FileData
import com.example.quotesapp.databinding.FragmentSentExchangeBinding
import com.example.quotesapp.presentation.adapter.ExchangeResponseAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.SentExchangeViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.HomeItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SentExchangeFragment : BaseFragment<FragmentSentExchangeBinding>(),HomeItemClickListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSentExchangeBinding =
        FragmentSentExchangeBinding::inflate

    private val viewModel by viewModels<SentExchangeViewModel>()

    override fun setup() {
        val userType = arguments?.getString(Constants.USER_TYPE)

        viewModel.getSentItems(userType.toString(),savedPrefManager.getToken().toString())
        viewModel.postLiveData.observe(viewLifecycleOwner){
            binding?.noExchange?.isVisible = it.data.isEmpty()
            binding?.recyclerView?.adapter = ExchangeResponseAdapter(it.data,requireContext(),this@SentExchangeFragment,requireParentFragment())
        }

    }


    override fun clicked(id: String,type: String) {
        when(type) {
            Constants.DOCUMENT -> {
                val pdfUrl = "http://143.110.247.128:8008/user/document/$id"
                val intent
                        : Intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
                    .addCategory(Intent.CATEGORY_BROWSABLE)

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity (intent)
            }
            Constants.QUOTE,Constants.IMAGE->{
                findNavController().navigate(ExchangeFragmentDirections.actionExchangeFragmentToDisplayFragment(type = type, name = id,isExchnage = true))
            }
        }
    }

    override fun clicked(type: String) {

    }

    override fun openEditPage(fileData: FileData) {
    }

}