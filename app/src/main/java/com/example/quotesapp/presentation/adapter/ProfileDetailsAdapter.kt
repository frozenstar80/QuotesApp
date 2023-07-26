package com.example.quotesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.quotesapp.databinding.ItemProfileBinding

class ProfileDetailsAdapter(
    val context: Context,
    var data: List<Pair<String,String>>
) :
    RecyclerView.Adapter<ProfileDetailsAdapter.SearchUsersViewHolder>() {


    class SearchUsersViewHolder(val itemBinding: ViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: SearchUsersViewHolder, position: Int) {

        with(holder) {
            val result= data[position]
            (this.itemBinding as ItemProfileBinding).heading.text = result.first

            itemBinding.subheading.text = result.second
    }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
