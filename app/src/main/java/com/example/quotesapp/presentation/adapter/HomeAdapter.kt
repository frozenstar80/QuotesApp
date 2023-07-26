package com.example.quotesapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.quotesapp.R
import com.example.quotesapp.data.FileData
import com.example.quotesapp.databinding.ItemCreatefirstdocBinding
import com.example.quotesapp.databinding.ItemCreatefirstpicBinding
import com.example.quotesapp.databinding.ItemCreatefirstquoteBinding
import com.example.quotesapp.databinding.ItemCreatefirstvideoBinding
import com.example.quotesapp.databinding.ItemHomedocBinding
import com.example.quotesapp.databinding.ItemHomepicBinding
import com.example.quotesapp.databinding.ItemHomequoteBinding
import com.example.quotesapp.databinding.ItemHomevideoBinding
import com.example.quotesapp.presentation.fragment.dashboard.HomeFragmentDirections
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener
import com.example.quotesapp.util.SavedPrefManager

class HomeAdapter(
    val data: List<FileData> = arrayListOf(),
    val context: Context,
    private val searchUserItemClickListener: HomeItemClickListener,
    val  fragment:Fragment
) :
    RecyclerView.Adapter<HomeAdapter.SearchUsersViewHolder>() {

    private val image = 1
    private val video = 2
    private val document = 3
    private val quote = 4
    private val eimage = 5
    private val evideo = 6
    private val edocument = 7
    private val equote = 8

    class SearchUsersViewHolder( val itemBinding: ViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun getItemViewType(position: Int): Int {
        var type = 1
        when (data[position].type) {
            Constants.IMAGE -> type = image
            Constants.VIDEO -> type  = video
            Constants.DOCUMENT -> type = document
            Constants.QUOTE -> type = quote
            Constants.EMPTY+Constants.IMAGE -> type = eimage
            Constants.EMPTY+Constants.VIDEO -> type  = evideo
            Constants.EMPTY+Constants.DOCUMENT -> type = edocument
            Constants.EMPTY+Constants.QUOTE -> type = equote
        }

        return  type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return when(viewType) {
          image ->  SearchUsersViewHolder(
                ItemHomepicBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
          video -> SearchUsersViewHolder(
              ItemHomevideoBinding.inflate(
                  LayoutInflater.from(parent.context),
                  parent,
                  false
              )
          )
            document ->  SearchUsersViewHolder(
                ItemHomedocBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            quote-> SearchUsersViewHolder(
                ItemHomequoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            eimage ->  SearchUsersViewHolder(
                ItemCreatefirstpicBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            evideo -> SearchUsersViewHolder(
                ItemCreatefirstvideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            edocument ->  SearchUsersViewHolder(
                ItemCreatefirstdocBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            equote-> SearchUsersViewHolder(
                ItemCreatefirstquoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> {
                SearchUsersViewHolder(
                    ItemHomepicBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchUsersViewHolder, position: Int) {

        val result = data[position]

        with(holder) {
            when (holder.itemViewType) {
                video->{
                    (itemBinding as ItemHomevideoBinding).txtUser.text = result.user?.fullName
                    itemBinding.txtVideo.text  = result.title
                    if (result.user?.photos?.isNotEmpty() == true)
                    GlideLoader(context).loadPicFromWeb(result.user?.photos?.get(0),itemBinding.imgProfilePic)
                    GlideLoader(context).loadUserPicture("http://143.110.247.128:8008/user/video/"+result.name,itemBinding.imgVideo)
                    itemBinding.txtViewAll.setOnClickListener {
                        searchUserItemClickListener.clicked(result.type.toString())
                    }
                    itemBinding.imgVideo.setOnClickListener {
                        fragment.findNavController().navigate(HomeFragmentDirections.actionHomeToVideoPlayerBottomSheetFragment(result.name.toString()))
                    }
                }

                image ->{
                    (itemBinding as ItemHomepicBinding).txtPhoto.text = result.title
                    itemBinding.txtUser.text = result.user?.fullName
                    if (result.user?.photos?.isNotEmpty() == true)
                    GlideLoader(context).loadPicFromWeb(result.user?.photos?.get(0),itemBinding.imgProfilePic)
                    GlideLoader(context).loadPicFromWeb(result.name,itemBinding.imgPhoto)
                    itemBinding.imgPhoto.setOnClickListener {
                        searchUserItemClickListener.clicked(result.name.toString(),result.type.toString())
                    }
                    itemBinding.txtViewAll.setOnClickListener {
                        searchUserItemClickListener.clicked(result.type.toString())
                    }
                }
                document->{
                    (itemBinding as ItemHomedocBinding).documentTitle.text = result.title
                    itemBinding.txtUser.text = result.user?.fullName
                    itemBinding.documentName.text = result.name
                    GlideLoader(context).loadPicFromWeb(result.user?.photos?.get(0),itemBinding.imgProfilePic)
                    itemBinding.documentName.setOnClickListener {
                        searchUserItemClickListener.clicked(result.name.toString(),result.type.toString())
                    }
                    itemBinding.txtViewAll.setOnClickListener {
                        searchUserItemClickListener.clicked(result.type.toString())
                    }
                }
                quote->{
                    (itemBinding as ItemHomequoteBinding).txtQuote.text = result.content
                    itemBinding.txtUser.text = result.user?.fullName
                    if (result.user?.photos?.isNotEmpty() == true)
                    GlideLoader(context).loadPicFromWeb(result.user?.photos?.get(0),itemBinding.imgProfilePic)
                    GlideLoader(context).loadPicFromWeb(result.file,itemBinding.imgQuotes)
                    itemBinding.txtViewAll.setOnClickListener {
                        searchUserItemClickListener.clicked(result.type.toString())
                    }
                    if (result?.file?.isEmpty() == true) itemBinding.imgQuotes.isVisible =  false
                    itemBinding.imgQuotes.setOnClickListener {
                        searchUserItemClickListener.clicked(result.file.toString(),result.type.toString())
                    }
                }
                evideo->{
                    setTextInVideo(itemBinding as ItemCreatefirstvideoBinding)
                        GlideLoader(context).loadPicFromWeb(SavedPrefManager(context).getPhoto(),itemBinding.imgProfilePic)
                    itemBinding.txtUser.text = SavedPrefManager(context).getFullName()

                }

                eimage ->{
                   setTextInPicture(itemBinding as ItemCreatefirstpicBinding)

                        GlideLoader(context).loadPicFromWeb(SavedPrefManager(context).getPhoto(),itemBinding.imgProfilePic)
                    itemBinding.txtUser.text = SavedPrefManager(context).getFullName()
                }
                edocument->{
                  setTextInDocument(itemBinding as ItemCreatefirstdocBinding)

                        GlideLoader(context).loadPicFromWeb(SavedPrefManager(context).getPhoto(),itemBinding.imgProfilePic)
                    itemBinding.txtUser.text = SavedPrefManager(context).getFullName()
                }
                equote->{
                   setTextInQuote(itemBinding as ItemCreatefirstquoteBinding)
                        GlideLoader(context).loadPicFromWeb(SavedPrefManager(context).getPhoto(),itemBinding.imgProfilePic)
                    itemBinding.txtUser.text = SavedPrefManager(context).getFullName()
                }
            }
        }
    }

    private fun setTextInVideo(binding: ItemCreatefirstvideoBinding) {
        val text1 = context.getString(R.string.upload_your_first_video_by_clicking) + " "
        val text2 = " "+ context.getString(R.string.button)
        val image = context.getDrawable(R.drawable.ic_fab)
        val imageSize = (binding.txtContent.lineHeight.times(1.25)).toInt()
        image?.setBounds(0, 0, imageSize, imageSize)
        val spannableString = SpannableString(text1 + text2)
        spannableString.setSpan(
            image?.let { ImageSpan(it) },
            text1.length,
            text1.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtContent.text = spannableString

    }

    private fun setTextInDocument(binding: ItemCreatefirstdocBinding) {
        val text1 = context.getString(R.string.save_your_first_document_by_clicking) + " "
        val text2 = " "+ context.getString(R.string.button)
        val image = context.getDrawable(R.drawable.ic_fab)
        val imageSize = (binding.txtContent.lineHeight.times(1.25)).toInt()
        image?.setBounds(0, 0, imageSize, imageSize)
        val spannableString = SpannableString(text1 + text2)
        spannableString.setSpan(
            image?.let { ImageSpan(it) },
            text1.length,
            text1.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtContent.text = spannableString

    }

    private fun setTextInPicture(binding: ItemCreatefirstpicBinding) {
        val text1 = context.getString(R.string.upload_your_first_picture_by_clicking) + " "
        val text2 = " "+  context.getString(R.string.button)
        val image = context.getDrawable(R.drawable.ic_fab)
        val imageSize = (binding.txtContent.lineHeight.times(1.25)).toInt()
        image?.setBounds(0, 0, imageSize, imageSize)
        val spannableString = SpannableString(text1 + text2)
        spannableString.setSpan(
            image?.let { ImageSpan(it) },
            text1.length,
            text1.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtContent.text = spannableString

    }

    private fun setTextInQuote(binding: ItemCreatefirstquoteBinding) {
        val text1 = context.getString(R.string.create_your_first_own_quotes_by_clicking) + " "
        val text2 = " " + context.getString(R.string.button)
        val image = context.getDrawable(R.drawable.ic_fab)
        val imageSize = (binding.txtContent.lineHeight.times(1.25)).toInt()
        image?.setBounds(0, 0, imageSize, imageSize)
        val spannableString = SpannableString(text1 + text2)
        spannableString.setSpan(
            image?.let { ImageSpan(it) },
            text1.length,
            text1.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtContent.text = spannableString
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
