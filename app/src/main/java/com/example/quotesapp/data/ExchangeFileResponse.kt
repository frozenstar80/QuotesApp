package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class ExchangeFileResponse(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<ExchangeFileData> = arrayListOf()
)
data class ExchangeFileData (

    @SerializedName("_id"          ) var Id         : String?   = null,
    @SerializedName("caption"      ) var caption    : String?   = null,
    @SerializedName("item"         ) var item       : String?   = null,
    @SerializedName("type"         ) var type       : String?   = null,
    @SerializedName("user_type"    ) var userType   : String?   = null,
    @SerializedName("to_user_id"   ) var toUserId   : String?   = null,
    @SerializedName("from_user_id" ) var fromUserId : String?   = null,
    @SerializedName("to_user"      ) var toUser     : ToUser?   = ToUser(),
    @SerializedName("from_user"    ) var fromUser   : FromUser? = FromUser()

)

