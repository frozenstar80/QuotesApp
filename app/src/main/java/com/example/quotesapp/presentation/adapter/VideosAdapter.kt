package com.example.quotesapp.presentation.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.FileData
import com.example.quotesapp.databinding.ItemVideosBinding
import com.example.quotesapp.presentation.fragment.dashboard.VideosFragmentDirections
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener


class VideosAdapter(
    val context: Context,
    var data: List<FileData> = arrayListOf(),
    val searchUserItemClickListener: HomeItemClickListener,
    val isEditable: Boolean = false,
    val fragment:Fragment,
    val isMemberPlus:Boolean
) :
    RecyclerView.Adapter<VideosAdapter.SearchUsersViewHolder>() {


    class SearchUsersViewHolder(val itemBinding: ItemVideosBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemVideosBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: SearchUsersViewHolder, position: Int) {

        with(holder) {
            val result = data[position]
            if(isEditable) itemBinding.imgEdit.isVisible = true
            itemBinding.imgEdit.setOnClickListener {
                searchUserItemClickListener.openEditPage(result)
            }
            itemBinding.imgVideo.setOnClickListener {
                fragment.findNavController().navigate(VideosFragmentDirections.actionVideoToVideoPlayerBottomSheetFragment(result.name.toString()))
            }
            itemBinding.txtUser.text = result.user?.fullName
            if (isMemberPlus){
                itemBinding.txtDesc.text=context.getString(R.string.member_plus_caps)
            }
            itemBinding.txtVideo.text  = result.title
            GlideLoader(context).loadPicFromWeb(result.user?.photos?.get(0),itemBinding.imgProfilePic)
             GlideLoader(context).loadUserPicture("http://143.110.247.128:8008/user/video/"+result.name,itemBinding.imgVideo)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }
}
