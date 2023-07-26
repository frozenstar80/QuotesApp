package com.example.quotesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.quotesapp.data.UserDataResponse
import com.example.quotesapp.databinding.ItemFollowBinding
import com.example.quotesapp.util.FollowItemClickListener
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.SearchUserItemClickListener

class FollowersFollowingAdapter(
    val context: Context,
    var data: List<UserDataResponse> = arrayListOf(),
    private val followItemClickListener: FollowItemClickListener
) :
    RecyclerView.Adapter<FollowersFollowingAdapter.SearchUsersViewHolder>() {


    class SearchUsersViewHolder(val itemBinding: ItemFollowBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemFollowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: SearchUsersViewHolder, position: Int) {

        with(holder) {
            val result= data[position]
            itemBinding.txtUser.text =  result.fullName
            if (result.photos.isNotEmpty())
            GlideLoader(context).loadPicFromWeb(result.photos[0],itemBinding.imgProfilePic)
            itemBinding.remove.setOnClickListener {
                followItemClickListener.delete(result.Id.toString())
            }
            itemBinding.imgProfilePic.setOnClickListener {
                followItemClickListener.viewProfile(result.Id.toString())
            }

            }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
