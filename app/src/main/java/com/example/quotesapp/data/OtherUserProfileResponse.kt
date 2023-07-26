package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class OtherUserProfileResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : UserDataResponse?    = UserDataResponse()
)
