package com.example.quotesapp.presentation.fragment.dashboard
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.R
import com.example.quotesapp.data.FileData
import com.example.quotesapp.databinding.FragmentHomeBinding
import com.example.quotesapp.presentation.adapter.HomeAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.DocumentsViewModel
import com.example.quotesapp.presentation.viewModel.HomeSharedViewModel
import com.example.quotesapp.presentation.viewModel.NotificationViewModel
import com.example.quotesapp.presentation.viewModel.PhotosViewModel
import com.example.quotesapp.presentation.viewModel.QuotesViewModel
import com.example.quotesapp.presentation.viewModel.VideosViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() , HomeItemClickListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    private val videoviewModel by viewModels<VideosViewModel>()
    private val photoviewModel by viewModels<PhotosViewModel>()
    private val docviewModel by viewModels<DocumentsViewModel>()
    private val quoteviewModel by viewModels<QuotesViewModel>()
    val viewModel by viewModels<NotificationViewModel>()
    private val homeSharedViewModel by activityViewModels<HomeSharedViewModel>()
    private var isPictureObserved = false
    private var isDocumentObserved = false
    private var isVideoObserved=false
    private var arraylist : MutableList<FileData>?  =  arrayListOf<FileData>()

    override fun setup() {

        homeSharedViewModel.isDataChange.observe(this){
            if (it==true){
                arraylist=null
                binding?.rvHome?.removeAllViews()
                val adapter = HomeAdapter(arrayListOf(),requireContext(), this,this)
                binding?.rvHome?.adapter =adapter
                photoviewModel.getDocuments(Constants.IMAGE,savedPrefManager.getId().toString(),savedPrefManager.getToken().toString(),"user")
            }
        }

        val adapter = arraylist?.let { HomeAdapter(it,requireContext(), this,this) }
        binding?.rvHome?.adapter =adapter

        viewModel.showNotificationCount(savedPrefManager.getToken().toString())
        viewModel.getNotificationCountResponse.observe(viewLifecycleOwner){
            if ((it.data ?: 0) > 0){
                binding?.notificationCount?.isVisible = true
                binding?.notificationCount?.text = it.data.toString()
            }
            else
                binding?.notificationCount?.isVisible = false
        }
        binding?.txtSearchView?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToSearchPeopleFragment())
        }
        binding?.imgNotification?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToNotificationFragment())
        }

        GlideLoader(requireContext()).loadPicFromWeb(savedPrefManager.getPhoto(),binding?.imgProfilePic)
        binding?.txtUserName?.text = savedPrefManager.getFullName()
        binding?.imgProfilePic?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToProfileDetailsFragment())
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

                binding?.btnDocuments?.visibility = View.VISIBLE
                binding?.btnDocuments?.startAnimation(popOutAnimation)

                binding?.btnPhoto?.visibility = View.VISIBLE
                binding?.btnPhoto?.startAnimation(popOutAnimation)

                binding?.btnQuote?.visibility = View.VISIBLE
                binding?.btnQuote?.startAnimation(popOutAnimation)
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

                binding?.btnQuote?.startAnimation(popBackAnimation)
                binding?.btnQuote?.visibility = View.INVISIBLE
            }
        }

        binding?.btnQuote?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToUploadDataFragment(dataType = Constants.QUOTE))
        }
        binding?.btnVideo?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToUploadDataFragment(dataType = Constants.VIDEO))
        }
        binding?.btnDocuments?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToUploadDataFragment(dataType = Constants.DOCUMENT))
        }
        binding?.btnPhoto?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToUploadDataFragment(dataType = Constants.IMAGE))
        }
        binding?.imgLink?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToBurialItemsHostFragment("user",savedPrefManager.getId().toString(),"links",false))
        }
        binding?.imgMotivation?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToBurialItemsHostFragment("user",savedPrefManager.getId().toString(),"motivations",false))
        }
        binding?.imgBurial?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToBurialItemsHostFragment("user",savedPrefManager.getId().toString(),"burials",false))
        }
        binding?.imgIdeas?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToBurialItemsHostFragment("user",savedPrefManager.getId().toString(),"ideas",false))
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
                findNavController().navigate(HomeFragmentDirections.actionHomeToDisplayFragment(type = type, name = id))
            }
    }
    }

    override fun clicked(type:String) {
       when(type){
           Constants.QUOTE -> findNavController().navigate(HomeFragmentDirections.actionHomeToQuotesFragment(id="",
               sourceInside = true
           ))
           Constants.IMAGE -> findNavController().navigate(HomeFragmentDirections.actionHomeToPhoto(id="",
               sourceInside = true
           ))
           Constants.DOCUMENT -> findNavController().navigate(HomeFragmentDirections.actionHomeToDocuments(id="",
               sourceInside = true
           ))
           Constants.VIDEO -> findNavController().navigate(HomeFragmentDirections.actionHomeToVideo(id="",
               sourceInside = true
           ))
       }
    }

    override fun openEditPage(fileData: FileData) {

    }

    override fun onResume() {
        super.onResume()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.VISIBLE
        photoviewModel.getDocuments(Constants.IMAGE,savedPrefManager.getId().toString(),savedPrefManager.getToken().toString(),"user")
        photoviewModel.getDocumentsLiveData.observe(viewLifecycleOwner){list->
            arraylist= arrayListOf()
            arraylist?.forEach {
                if (it.type== Constants.EMPTY+Constants.IMAGE || it.type == Constants.IMAGE)
                    return@observe
            }
            if (list.data.isNotEmpty()) {
                arraylist?.add(list.data[0])
            }
            else {
                val fileData = FileData(type = Constants.EMPTY+Constants.IMAGE)
                arraylist?.add(fileData)
            }
            videoviewModel.getDocuments(Constants.VIDEO,savedPrefManager.getId().toString(),savedPrefManager.getToken().toString(),"user")
            isPictureObserved=true
        }
        videoviewModel.getDocumentsLiveData.observe(viewLifecycleOwner){list->
            if (isPictureObserved) {
                arraylist?.forEach {
                    if (it.type == Constants.EMPTY + Constants.VIDEO || it.type == Constants.VIDEO)
                        return@observe
                }
                if (list.data.isNotEmpty()) {
                    arraylist?.add(list.data[0])
                } else {
                    val fileData = FileData(type = Constants.EMPTY + Constants.VIDEO)
                    arraylist?.add(fileData)
                }
                docviewModel.getDocuments(
                    Constants.DOCUMENT,
                    savedPrefManager.getId().toString(),
                    savedPrefManager.getToken().toString(),
                    "user"
                )
                isVideoObserved=true
            }
        }
        docviewModel.getDocumentsLiveData.observe(viewLifecycleOwner){list->
            if (isVideoObserved) {
                arraylist?.forEach {
                    if (it.type == Constants.EMPTY + Constants.DOCUMENT || it.type == Constants.DOCUMENT)
                        return@observe
                }
                if (list.data.isNotEmpty()) {
                    arraylist?.add(list.data[0])
                } else {
                    val fileData = FileData(type = Constants.EMPTY + Constants.DOCUMENT)
                    arraylist?.add(fileData)
                }
                quoteviewModel.getQuotes(
                    savedPrefManager.getToken().toString(),
                    savedPrefManager.getId().toString(),
                    "user"
                )
                isDocumentObserved=true
            }
        }
        quoteviewModel.getDocumentsLiveData.observe(viewLifecycleOwner){list->
            if (isDocumentObserved) {
                arraylist?.forEach {
                    if (it.type == Constants.EMPTY + Constants.QUOTE || it.type == Constants.QUOTE)
                        return@observe
                }
                if (list.data.isNotEmpty()) {
                    arraylist?.add(list.data[0].copy(type = Constants.QUOTE))
                } else {
                    val fileData = FileData(type = Constants.EMPTY + Constants.QUOTE)
                    arraylist?.add(fileData)
                }
                val adapter = arraylist?.let { HomeAdapter(it, requireContext(), this, this) }
                binding?.rvHome?.adapter = adapter
            }
            isPictureObserved=false
            isVideoObserved=false
            isDocumentObserved=false
        }
    }

    override fun onPause() {
        super.onPause()
        arraylist?.clear()
    }

}