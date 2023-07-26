package com.example.quotesapp.presentation.fragment.exchange

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentOtherUserExchangeBinding
import com.example.quotesapp.databinding.ItemCreatefirstpicBinding
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.fragment.dashboard.HomeFragmentDirections
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.SavedPrefManager

class OtherUserExchangeFragment : BaseFragment<FragmentOtherUserExchangeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOtherUserExchangeBinding =
        FragmentOtherUserExchangeBinding::inflate

    private val nav by navArgs<OtherUserExchangeFragmentArgs>()

    override fun setup() {

        val isNormalUser  = nav.userType=="user"

        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }


        setTextInPicture()

        GlideLoader(requireContext()).loadPicFromWeb(SavedPrefManager(context).getPhoto(),binding?.imgProfilePic)
        binding?.txtUser?.text = SavedPrefManager(context).getFullName()

        val popOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_out_animation)
        val popBackAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_back_animation)
        val forward = AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_forward_animation)
        val backward = AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_backward_animation)

        binding?.imgFab?.setOnClickListener {
            if (binding?.btnVideo?.isVisible == false) {
                binding?.viewTransparent?.isVisible = true

                binding?.imgFab?.startAnimation(forward)

                // Show the four buttons with the "pop out" animation
                binding?.btnVideo?.visibility = View.VISIBLE
                binding?.btnVideo?.startAnimation(popOutAnimation)

                binding?.btnDocuments?.visibility = View.VISIBLE
                binding?.btnDocuments?.startAnimation(popOutAnimation)

                binding?.btnPhoto?.visibility = View.VISIBLE
                binding?.btnPhoto?.startAnimation(popOutAnimation)

            } else {
                binding?.viewTransparent?.isVisible = false
                binding?.imgFab?.startAnimation(backward)
                // Hide the four buttons with the "pop back" animation
                binding?.btnVideo?.startAnimation(popBackAnimation)
                binding?.btnVideo?.visibility = View.INVISIBLE

                binding?.btnDocuments?.startAnimation(popBackAnimation)
                binding?.btnDocuments?.visibility = View.INVISIBLE

                binding?.btnPhoto?.startAnimation(popBackAnimation)
                binding?.btnPhoto?.visibility = View.INVISIBLE

            }
        }

        binding?.btnVideo?.setOnClickListener {
            findNavController().navigate(OtherUserExchangeFragmentDirections.actionOtherUserExchangeFragmentToUploadDataFragment(dataType = Constants.VIDEO, isExchangeFragment = 1,isNormalUser = isNormalUser, id = nav.id))
        }
        binding?.btnDocuments?.setOnClickListener {
            findNavController().navigate(OtherUserExchangeFragmentDirections.actionOtherUserExchangeFragmentToUploadDataFragment(dataType = Constants.DOCUMENT, isExchangeFragment = 1,isNormalUser = isNormalUser,id=nav.id))
        }
        binding?.btnPhoto?.setOnClickListener {
            findNavController().navigate(OtherUserExchangeFragmentDirections.actionOtherUserExchangeFragmentToUploadDataFragment(dataType = Constants.IMAGE, isExchangeFragment = 1,isNormalUser = isNormalUser,id=nav.id))
        }
    }

    private fun setTextInPicture() {
        val text1 = requireContext().getString(R.string.upload_your_first_exchange_by_clicking) + " "
        val text2 = " "+  requireContext().getString(R.string.button)
        val image = requireContext().getDrawable(R.drawable.ic_fab)
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

}
