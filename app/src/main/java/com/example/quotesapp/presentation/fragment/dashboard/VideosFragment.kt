package com.example.quotesapp.presentation.fragment.dashboard

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
import com.example.quotesapp.databinding.FragmentVideosBinding
import com.example.quotesapp.presentation.adapter.VideosAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.VideosViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener
import com.example.quotesapp.util.SavedPrefManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideosFragment : BaseFragment<FragmentVideosBinding>(),HomeItemClickListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentVideosBinding =
        FragmentVideosBinding::inflate

    val viewModel by viewModels<VideosViewModel>()
    private val args by navArgs<VideosFragmentArgs>()
    private var sourceInside = false

    override fun setup() {
         val isEditable = args.otherUser=="no"
        sourceInside = args.sourceInside
        if (!sourceInside){
            binding?.imgBack?.isVisible=false
        }
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModel.apply {
            if (args.isMemberPlus)
                getDocuments("video","${args.id.ifEmpty { savedPrefManager.getId() }}",savedPrefManager.getToken().toString(),"memberplus")
            else
                getDocuments("video","${args.id.ifEmpty { savedPrefManager.getId() }}",savedPrefManager.getToken().toString(),"user")
            getDocumentsLiveData.observe(viewLifecycleOwner){
                if (it.data.isEmpty() && args.otherUser=="no"){
                    binding?.cardView?.isVisible = true
                    setTextInVideo()
                    binding?.rvVideos?.isVisible = false
                    GlideLoader(requireContext()).loadPicFromWeb(SavedPrefManager(context).getPhoto(),binding?.imgProfilePic)
                    binding?.txtUser?.text = SavedPrefManager(context).getFullName()
                }else{
                    binding?.cardView?.isVisible = false
                    binding?.rvVideos?.isVisible = true
                binding?.rvVideos?.adapter = VideosAdapter(requireContext(),it.data,this@VideosFragment,isEditable,this@VideosFragment,args.isMemberPlus)
            }
                binding?.noItem?.isVisible = it.data.isEmpty() && args.otherUser!="no"
            }
        }
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
            } else {
                binding?.viewTransparent?.isVisible = false
                binding?.imgFab?.startAnimation(backward)
                // Hide the four buttons with the "pop back" animation
                binding?.btnVideo?.startAnimation(popBackAnimation)
                binding?.btnVideo?.visibility = View.INVISIBLE

            }
        }

        binding?.btnVideo?.setOnClickListener {
            findNavController().navigate(VideosFragmentDirections.actionVideoToUploadDataFragment(dataType = Constants.VIDEO,
                0,
                isNormalUser = !args.isMemberPlus,
                id = args.id))
        }
    }

    override fun clicked(id: String, type: String) {
        findNavController().navigate(VideosFragmentDirections.actionVideoToDisplayFragment(type = type, name = id))
    }

    override fun clicked(type: String) {

    }

    override fun openEditPage(fileData: FileData) {
        findNavController().navigate(VideosFragmentDirections.actionVideoToUploadDataFragment(
            Constants.VIDEO, documentImageVideoQuoteId = fileData.Id.toString(),
            isNormalUser = !args.isMemberPlus, file = fileData.name.toString(), title = fileData.title.toString()
        ))
    }

    private fun setTextInVideo() {
        val text1 = context?.getString(R.string.upload_your_first_video_by_clicking) + " "
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
        }else{
            val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
            view.visibility = View.VISIBLE
        }
    }
}