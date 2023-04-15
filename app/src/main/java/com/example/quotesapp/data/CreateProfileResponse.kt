package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class CreateProfileResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null
)
