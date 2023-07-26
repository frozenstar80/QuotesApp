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
import com.example.quotesapp.databinding.FragmentPhotosBinding
import com.example.quotesapp.presentation.adapter.PhotoAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.PhotosViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener
import com.example.quotesapp.util.SavedPrefManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : BaseFragment<FragmentPhotosBinding>(),HomeItemClickListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotosBinding =
        FragmentPhotosBinding::inflate


    val viewModel by viewModels<PhotosViewModel>()
    private val args by navArgs<PhotosFragmentArgs>()
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
                getDocuments("image","${args.id.ifEmpty { savedPrefManager.getId() }}",savedPrefManager.getToken().toString(),"memberplus")
            else
               getDocuments("image","${args.id.ifEmpty { savedPrefManager.getId() }}",savedPrefManager.getToken().toString(),"user")
            getDocumentsLiveData.observe(viewLifecycleOwner){
                   if (it.data.isEmpty() && args.otherUser=="no"){
                       binding?.cardView?.isVisible = true
                       setTextInPhotos()
                       binding?.rvPhoto?.isVisible = false
                       GlideLoader(requireContext()).loadPicFromWeb(SavedPrefManager(context).getPhoto(),binding?.imgProfilePic)
                       binding?.txtUser?.text = SavedPrefManager(context).getFullName()
                   }else{
                       binding?.cardView?.isVisible = false
                       binding?.rvPhoto?.isVisible = true
                binding?.rvPhoto?.adapter = PhotoAdapter(requireContext(),it.data,this@PhotosFragment,isEditable,args.isMemberPlus)
            }
                binding?.noItem?.isVisible = it.data.isEmpty() && args.otherUser!="no"
            }
        }
        val popOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_out_animation)
        val popBackAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_back_animation)
        val forward = AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_forward_animation)
        val backward = AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_backward_animation)

        binding?.imgFab?.setOnClickListener {
            if (binding?.btnPhoto?.isVisible == false) {
                binding?.viewTransparent?.isVisible = true

                binding?.imgFab?.startAnimation(forward)

                binding?.btnPhoto?.visibility = View.VISIBLE
                binding?.btnPhoto?.startAnimation(popOutAnimation)

            } else {
                binding?.viewTransparent?.isVisible = false
                binding?.imgFab?.startAnimation(backward)
                binding?.btnPhoto?.startAnimation(popBackAnimation)
                binding?.btnPhoto?.visibility = View.INVISIBLE
            }
        }
        binding?.btnPhoto?.setOnClickListener {
            findNavController().navigate(PhotosFragmentDirections.actionPhotoToUploadDataFragment(
                dataType = Constants.IMAGE,
                0,
                isNormalUser = !args.isMemberPlus,
                id = args.id
            ))
        }
    }

    override fun clicked(id: String, type: String) {
        findNavController().navigate(PhotosFragmentDirections.actionPhotoToDisplayFragment(type = type, name = id))
    }

    override fun clicked(type: String) {

    }

    override fun openEditPage(fileData: FileData) {
        findNavController().navigate(PhotosFragmentDirections.actionPhotoToUploadDataFragment(Constants.IMAGE, documentImageVideoQuoteId = fileData.Id.toString(), isNormalUser = !args.isMemberPlus,
            file = fileData.name.toString(),title = fileData.title.toString()))
    }

    private fun setTextInPhotos() {
        val text1 = context?.getString(R.string.upload_your_first_picture_by_clicking) + " "
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