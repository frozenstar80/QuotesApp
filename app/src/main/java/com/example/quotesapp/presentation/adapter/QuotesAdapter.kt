package com.example.quotesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.FileData
import com.example.quotesapp.databinding.ItemQuotesBinding
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener

class QuotesAdapter(
    val context: Context,
    var data: List<FileData> = arrayListOf(),
    val searchUserItemClickListener: HomeItemClickListener,
    val isEditable: Boolean = false,
    val isMemberPlus:Boolean
) :
    RecyclerView.Adapter<QuotesAdapter.SearchUsersViewHolder>() {


    class SearchUsersViewHolder(val itemBinding: ItemQuotesBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemQuotesBinding.inflate(
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
            if (isMemberPlus){
                itemBinding.txtDesc.text=context.getString(R.string.member_plus_caps)
            }
            itemBinding.txtQuote.text = result.content
            itemBinding.txtUser.text = result.user?.fullName
            if (result.file?.isEmpty() == true) itemBinding.imgQuotes.isVisible =  false
            GlideLoader(context).loadPicFromWeb(result.user?.photos?.get(0),itemBinding.imgProfilePic)
            GlideLoader(context).loadPicFromWeb(result.file,itemBinding.imgQuotes)
            itemBinding.imgQuotes.setOnClickListener {
                searchUserItemClickListener.clicked(result.file.toString(),result.type.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
