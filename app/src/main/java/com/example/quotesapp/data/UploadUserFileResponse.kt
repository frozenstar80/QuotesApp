package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class UploadUserFileResponse(

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()

)



data class Data (

    @SerializedName("name" ) var name : String? = null,
    @SerializedName("file" ) var file : String? = null

)
