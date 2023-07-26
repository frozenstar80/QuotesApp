package com.example.quotesapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.data.ViewAllQuotesData
import com.example.quotesapp.databinding.ItemQuotesIntroBinding
import com.example.quotesapp.util.SearchUserItemClickListener

class QuotesIntroAdapter(
    var data: List<ViewAllQuotesData> = arrayListOf(),
    val searchUserItemClickListener: SearchUserItemClickListener
) :
    RecyclerView.Adapter<QuotesIntroAdapter.SearchUsersViewHolder>() {


    class SearchUsersViewHolder(val itemBinding: ItemQuotesIntroBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemQuotesIntroBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: SearchUsersViewHolder, position: Int) {

        with(holder) {
            val result = data[position]
            itemBinding.imgEnabled.isVisible = result.isChecked
            itemBinding.quote.text = result.text
            itemBinding.quote.setOnClickListener {
                searchUserItemClickListener.clicked(result.id.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
