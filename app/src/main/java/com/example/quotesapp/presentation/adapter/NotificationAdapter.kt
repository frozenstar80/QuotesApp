package com.example.quotesapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.quotesapp.R
import com.example.quotesapp.data.DataNotification
import com.example.quotesapp.databinding.ItemNotificationAcceptedBinding
import com.example.quotesapp.databinding.ItemNotificationPendingBinding
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.NotificationItemClickListener

class NotificationAdapter(
    val context: Context,
    var data: List<DataNotification> = arrayListOf(),
    private val searchUserItemClickListener: NotificationItemClickListener
) :
    RecyclerView.Adapter<NotificationAdapter.SearchUsersViewHolder>() {

    private val requestAccepted = 1
    private val requestPending = 2

    class SearchUsersViewHolder(val itemBinding: ViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun getItemViewType(position: Int): Int {
        var type = 1
        val datatype = data[position].notificationState
        type = when (datatype) {
            "SENT" -> requestPending
            "ACCEPT" -> requestAccepted
            else -> requestAccepted
        }

        return  type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return when(viewType) {
          requestAccepted ->  SearchUsersViewHolder(
                ItemNotificationAcceptedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
          requestPending -> SearchUsersViewHolder(
              ItemNotificationPendingBinding.inflate(
                  LayoutInflater.from(parent.context),
                  parent,
                  false
              )
          )

            else -> {
                SearchUsersViewHolder(
                    ItemNotificationPendingBinding.inflate(
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
                requestPending -> {
                    (this.itemBinding as ItemNotificationPendingBinding).txtUser.text =
                        result.fromUser?.fullName + " " + context.getString(R.string.requested_to_follow_you)
                    itemBinding.txtFollower.text =
                        "${result.fromUser?.followers?.size} ${
                            context.getString(R.string.followers)
                        } • ${result.fromUser?.followers?.size} ${
                            context.getString(
                                R.string.following
                            )
                        }"
                    if (result.fromUser?.photos?.isNotEmpty() == true) GlideLoader(context).loadPicFromWeb(
                        result.fromUser?.photos?.get(0),
                        itemBinding.imgProfilePic
                    )

                    itemBinding.btnAccept.setOnClickListener {
                        searchUserItemClickListener.clicked(
                            result.Id.toString(),
                            Constants.ACCEPT
                        )
                    }
                    itemBinding.btnReject.setOnClickListener {
                        searchUserItemClickListener.clicked(
                            result.Id.toString(),
                            Constants.REJECT
                        )
                    }
                }

                requestAccepted -> {
                    (this.itemBinding as ItemNotificationAcceptedBinding).txtUser.text =
                        result.fromUser?.fullName.toString() +" "+ context.getString(R.string.started_following_you)
                    itemBinding.txtFollower.text =
                        "${result.fromUser?.followers?.size } ${
                            context.getString(R.string.followers)
                        } • ${result.fromUser?.followers?.size } ${
                            context.getString(
                                R.string.following
                            )
                        }"
                    if (result.fromUser?.photos?.isNotEmpty() == true) GlideLoader(context).loadPicFromWeb(
                        result.fromUser?.photos?.get(0),
                        itemBinding.imgProfilePic
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
