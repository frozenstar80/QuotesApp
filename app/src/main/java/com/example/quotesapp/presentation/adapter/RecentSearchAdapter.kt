package com.example.quotesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.quotesapp.R
import com.example.quotesapp.data.UserDataResponse
import com.example.quotesapp.databinding.ItemSearchPeopleBinding
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.SearchUserItemClickListener

class RecentSearchAdapter(
    val context: Context,
    var data: List<UserDataResponse> = arrayListOf(),
    val searchUserItemClickListener: SearchUserItemClickListener
) :
    RecyclerView.Adapter<RecentSearchAdapter.SearchUsersViewHolder>() {


    class SearchUsersViewHolder(val itemBinding: ViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemSearchPeopleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: SearchUsersViewHolder, position: Int) {

        with(holder) {
            val result= data[position]
            (this.itemBinding as ItemSearchPeopleBinding).userName.text = result.fullName
            itemBinding.userFollowing.text = "${if (result.followers.isNotEmpty()) result.followers.size else 0} ${context.getString(R.string.followers)} • ${if (result.following.isNotEmpty()) result.following.size else 0} ${context.getString(R.string.following)}"
            if (result.photos.isNotEmpty()) GlideLoader(context).loadPicFromWeb(result.photos[0],itemBinding.shapeableImageView)
           itemBinding.root.setOnClickListener {
               searchUserItemClickListener.clicked(result.Id.toString())
           }
            }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
