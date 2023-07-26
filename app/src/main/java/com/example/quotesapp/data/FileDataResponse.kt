package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class FileDataResponse(  @SerializedName("status"  ) var status  : Boolean?        = null,
                              @SerializedName("message" ) var message : String?         = null,
                              @SerializedName("data"    ) var data    : ArrayList<FileData> = arrayListOf())

data class FileData (

    @SerializedName("_id"       ) var Id        : String? = null,
    @SerializedName("type"      ) var type      : String? = null,
    @SerializedName("content"      ) var content      : String? = null,
    @SerializedName("file"      ) var file      : String? = null,
    @SerializedName("name"      ) var name      : String? = null,
    @SerializedName("title"     ) var title     : String? = null,
    @SerializedName("user_id"   ) var userId    : String? = null,
    @SerializedName("createdAt" ) var createdAt : String? = null,
    @SerializedName("updatedAt" ) var updatedAt : String? = null,
    @SerializedName("__v"       ) var _v        : Int?    = null,
    @SerializedName("user"      ) var user      : UserDataResponse?   = UserDataResponse()

)
