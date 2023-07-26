package com.example.quotesapp.presentation.fragment

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quotesapp.R
import com.example.quotesapp.data.FileData
import com.example.quotesapp.databinding.FragmentQuoteBinding
import com.example.quotesapp.presentation.adapter.QuotesAdapter
import com.example.quotesapp.presentation.viewModel.QuotesViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener
import com.example.quotesapp.util.SavedPrefManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuotesFragment : BaseFragment<FragmentQuoteBinding>(),HomeItemClickListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQuoteBinding =
        FragmentQuoteBinding::inflate

    val viewModel by viewModels<QuotesViewModel>()
    private val args by navArgs<QuotesFragmentArgs>()
    private var sourceInside = false

    override fun setup() {
        val isEditable = args.otherUser=="no" && args.sourceInside
        val userType  = if (args.isMemberPlus) "memberplus" else "user"
        sourceInside = args.sourceInside
        if (!sourceInside){
            binding?.imgBack?.isVisible=false
        }
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModel.apply {
          getQuotes(savedPrefManager.getToken().toString(),args.id.ifEmpty { savedPrefManager.getId().toString() },userType)
          getDocumentsLiveData.observe(viewLifecycleOwner){
              if (it.data.isEmpty() && args.otherUser=="no"){
                  binding?.cardView?.isVisible = true
                  setTextInQuote()
                  binding?.rvQuote?.isVisible = false
                  GlideLoader(requireContext()).loadPicFromWeb(SavedPrefManager(context).getPhoto(),binding?.imgProfilePic)
                  binding?.txtUser?.text = SavedPrefManager(context).getFullName()
              }else{
                  binding?.cardView?.isVisible = false
                  binding?.rvQuote?.isVisible = true
              binding?.rvQuote?.adapter = QuotesAdapter(requireContext(),it.data,this@QuotesFragment,isEditable,args.isMemberPlus)
          }}
      }

        val popOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_out_animation)
        val popBackAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_back_animation)
        val forward = AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_forward_animation)
        val backward = AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_backward_animation)

        binding?.imgFab?.setOnClickListener {
            if (binding?.btnQuote?.isVisible == false) {
                binding?.viewTransparent?.isVisible = true

                binding?.imgFab?.startAnimation(forward)


                binding?.btnQuote?.visibility = View.VISIBLE
                binding?.btnQuote?.startAnimation(popOutAnimation)
            } else {
                binding?.viewTransparent?.isVisible = false
                binding?.imgFab?.startAnimation(backward)

                binding?.btnQuote?.startAnimation(popBackAnimation)
                binding?.btnQuote?.visibility = View.INVISIBLE
            }
        }
        binding?.btnQuote?.setOnClickListener {
            findNavController().navigate(QuotesFragmentDirections.actionQuotesFragmentToUploadDataFragment(dataType = Constants.QUOTE,
                0,
                isNormalUser = !args.isMemberPlus,
                id = args.id))
        }

    }

    override fun clicked(id: String, type: String) {
        findNavController().navigate(QuotesFragmentDirections.actionQuotesFragmentToDisplayFragment(type = Constants.IMAGE, name = id))
    }

    override fun clicked(type: String) {

    }

    override fun openEditPage(fileData: FileData) {
        findNavController().navigate(QuotesFragmentDirections.actionQuotesFragmentToUploadDataFragment(
            Constants.QUOTE, documentImageVideoQuoteId = fileData.Id.toString(),
            isNormalUser = !args.isMemberPlus, file = fileData.file.toString(),
            title = fileData.content.toString()
        ))
    }

    private fun setTextInQuote() {
        val text1 = context?.getString(R.string.create_your_first_own_quotes_by_clicking) + " "
        val text2 = " "+ context?.getString(R.string.button)
        val image = context?.getDrawable(R.drawable.ic_fab)
        val imageSize = (binding?.txtContent?.lineHeight?.times(1.25))?.toInt()
        if (imageSize != null) {
            image?.setBounds(0, 0, imageSize, imageSize)
        }
        val spannableString = SpannableString(text1 + text2)
        spannableString.setSpan(
            image?.let { ImageSpan(it) },
            text1.length,
            text1.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding?.txtContent?.text = spannableString

    }

    override fun onResume() {
        super.onResume()
        if (sourceInside) {
                val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
                view.visibility = View.GONE
            }
    }
}