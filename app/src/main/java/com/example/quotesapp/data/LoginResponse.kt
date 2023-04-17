package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : LoginData?    = LoginData()
)
data class LoginData(
    @SerializedName("user"  ) var user  : UserDataResponse?   = UserDataResponse(),
    @SerializedName("token" ) var token : String? = null
)