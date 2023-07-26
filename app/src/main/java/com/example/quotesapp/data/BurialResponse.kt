package com.example.quotesapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BurialResponse (
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<BurialData> = arrayListOf()

) : Parcelable

@Parcelize
data class BurialData (

    @SerializedName("_id"       ) var Id        : String? = null,
    @SerializedName("title"     ) var title     : String? = null,
    @SerializedName("caption"   ) var caption   : String? = null,
    @SerializedName("tab"       ) var tab       : String? = null,
    @SerializedName("file"       ) var file       : String = "",
    @SerializedName("user_type" ) var userType  : String? = null,
    @SerializedName("user_id"   ) var userId    : String? = null,
    @SerializedName("createdAt" ) var createdAt : String? = null,
    @SerializedName("updatedAt" ) var updatedAt : String? = null

): Parcelable
