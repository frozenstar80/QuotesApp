package com.example.quotesapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.quotesapp.R
import com.example.quotesapp.data.ExchangeFileData
import com.example.quotesapp.databinding.ItemDocumentBinding
import com.example.quotesapp.databinding.ItemHomedocBinding
import com.example.quotesapp.databinding.ItemHomepicBinding
import com.example.quotesapp.databinding.ItemHomevideoBinding
import com.example.quotesapp.databinding.ItemPhotoBinding
import com.example.quotesapp.databinding.ItemVideosBinding
import com.example.quotesapp.presentation.fragment.exchange.ExchangeFragmentDirections
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener
import java.util.Locale

class ExchangeResponseAdapter(
    val data: List<ExchangeFileData> = arrayListOf(),
    val context: Context,
    private val searchUserItemClickListener: HomeItemClickListener,
    val  fragment:Fragment
) :
    RecyclerView.Adapter<ExchangeResponseAdapter.SearchUsersViewHolder>() {

    private val image = 1
    private val video = 2
    private val document = 3

    class SearchUsersViewHolder( val itemBinding: ViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun getItemViewType(position: Int): Int {
        var type = 1
        when (data[position].type) {
            Constants.IMAGE -> type = image
            Constants.VIDEO -> type  = video
            Constants.DOCUMENT -> type = document
        }

        return  type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return when(viewType) {
          image ->  SearchUsersViewHolder(
                ItemPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
          video -> SearchUsersViewHolder(
              ItemVideosBinding.inflate(
                  LayoutInflater.from(parent.context),
                  parent,
                  false
              )
          )
            document ->  SearchUsersViewHolder(
                ItemDocumentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> {
                SearchUsersViewHolder(
                    ItemPhotoBinding.inflate(
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
                    (itemBinding as ItemVideosBinding).txtUser.text = result.fromUser?.fullName
                    itemBinding.txtVideo.text  = result.caption
                    itemBinding.txtDesc.text = context.getString(R.string.you_shared_a) +" "+ result.type?.substring(0, 1)
                        ?.uppercase(
                            Locale.ROOT
                        ) + result.type?.substring(1) + " "+context.getString(R.string.with) + " " + result.toUser?.fullName
                    if (result.fromUser?.photos?.isNotEmpty() == true)
                    GlideLoader(context).loadPicFromWeb(result.fromUser?.photos?.get(0),itemBinding.imgProfilePic)
                    GlideLoader(context).loadUserPicture("http://143.110.247.128:8008/exchange/video/"+result.item,itemBinding.imgVideo)
                    itemBinding.imgVideo.setOnClickListener {
                        fragment.findNavController().navigate(ExchangeFragmentDirections.actionExchangeFragmentToVideoPlayerBottomSheetFragment(result.item.toString(),isExchange = true))

                    }
                }

                image ->{
                    (itemBinding as ItemPhotoBinding).txtPhoto.text = result.caption
                    itemBinding.txtUser.text = result.fromUser?.fullName
                    itemBinding.txtDesc.text = context.getString(R.string.you_shared_a) + result.type?.substring(0, 1)
                        ?.uppercase(
                            Locale.ROOT
                        ) + result.type?.substring(1) + " "+context.getString(R.string.with) + " " + result.toUser?.fullName
                    if (result.fromUser?.photos?.isNotEmpty() == true)
                    GlideLoader(context).loadPicFromWeb(result.fromUser?.photos?.get(0),itemBinding.imgProfilePic)
                    GlideLoader(context).loadUserPicture("http://143.110.247.128:8008/exchange/image/"+result.item,itemBinding.imgPhoto)
                    itemBinding.imgPhoto.setOnClickListener {
                        searchUserItemClickListener.clicked(result.item.toString(),result.type.toString())
                    }
                }
                document->{
                    (itemBinding as ItemDocumentBinding).documentTitle.text = result.caption
                    itemBinding.txtUser.text = result.fromUser?.fullName
                    itemBinding.txtDesc.text = context.getString(R.string.you_shared_a) +" "+ result.type?.substring(0, 1)
                        ?.uppercase(
                            Locale.ROOT
                        ) + result.type?.substring(1) + " "+context.getString(R.string.with) + " " + result.toUser?.fullName
                    itemBinding.documentName.text = result.item
                    GlideLoader(context).loadUserPicture(result.fromUser?.photos?.get(0),itemBinding.imgProfilePic)
                    itemBinding.documentName.setOnClickListener {
                        searchUserItemClickListener.clicked("http://143.110.247.128:8008/exchange/document/"+result.item,result.type.toString())
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
