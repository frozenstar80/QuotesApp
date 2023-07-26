package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class RecentSearchResponse(@SerializedName("status"  ) var status  : Boolean? = null,
                                @SerializedName("message" ) var message : String?  = null,
                                @SerializedName("data"    ) var data    : RecentSearchData?    = RecentSearchData()
)
data class RecentSearchData(
    @SerializedName("_id"       ) var Id        : String?           = null,
    @SerializedName("search_by" ) var searchBy  : String?           = null,
    @SerializedName("__v"       ) var _v        : Int?              = null,
    @SerializedName("createdAt" ) var createdAt : String?           = null,
    @SerializedName("keywords"  ) var keywords  : ArrayList<String> = arrayListOf(),
    @SerializedName("updatedAt" ) var updatedAt : String?           = null,
    @SerializedName("user_id"   ) var userId    : ArrayList<String> = arrayListOf(),
    @SerializedName("users"     ) var users     : ArrayList<UserDataResponse>  = arrayListOf()

)
