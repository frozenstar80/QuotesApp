package com.example.quotesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.FileData
import com.example.quotesapp.databinding.ItemDocumentBinding
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.HomeItemClickListener

class DocumentsAdapter(
    val context: Context,
    var data: List<FileData> = arrayListOf(),
    private val searchUserItemClickListener: HomeItemClickListener,
    private val isEditable: Boolean = false,
    val isMemberPlus:Boolean
) :
    RecyclerView.Adapter<DocumentsAdapter.SearchUsersViewHolder>() {


    class SearchUsersViewHolder(val itemBinding: ItemDocumentBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemDocumentBinding.inflate(
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
            GlideLoader(context).loadPicFromWeb(result.user?.photos?.get(0),itemBinding.imgProfilePic)
            itemBinding.documentTitle.text = result.title
            itemBinding.txtUser.text = result.user?.fullName
            itemBinding.documentName.text = result.name
            itemBinding.documentName.setOnClickListener {
                searchUserItemClickListener.clicked(result.name.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
