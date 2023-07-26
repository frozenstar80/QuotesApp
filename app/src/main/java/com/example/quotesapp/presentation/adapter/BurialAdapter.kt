package com.example.quotesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.data.BurialData
import com.example.quotesapp.databinding.ItemBurialBinding
import com.example.quotesapp.util.BurialItemClickListener
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader

class BurialAdapter(
    val context: Context,
    var data: List<BurialData> = arrayListOf(),
    private val burialItemClickListener: BurialItemClickListener,
    private val isEditable: Boolean = false,
private val type:String
) :
    RecyclerView.Adapter<BurialAdapter.SearchUsersViewHolder>() {


    class SearchUsersViewHolder(val itemBinding: ItemBurialBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemBurialBinding.inflate(
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
                burialItemClickListener.clicked(result)
            }
            itemBinding.txtTitle.text = result.title
            if (type==Constants.MOTIVATION) itemBinding.txtTitle.isVisible  =false
            itemBinding.txtCaption.text = result.caption
            if (result.file.isEmpty()) itemBinding.imgQuotes.isVisible =  false
            GlideLoader(context).loadPicFromWeb(result.file,itemBinding.imgQuotes)
            itemBinding.imgQuotes.setOnClickListener {
                burialItemClickListener.clicked(result.file)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
