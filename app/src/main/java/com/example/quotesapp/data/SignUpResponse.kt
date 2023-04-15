package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : SignUpData?    = SignUpData()
)
data class SignUpData(
    @SerializedName("user" ) var user : SignUpUser? = SignUpUser()
)
data class SignUpUser(
    @SerializedName("username"                ) var username            : String?           = null,
@SerializedName("first_name"              ) var firstName           : String?           = null,
@SerializedName("last_name"               ) var lastName            : String?           = null,
@SerializedName("photos"                  ) var photos              : ArrayList<String> = arrayListOf(),
@SerializedName("documents"               ) var documents           : ArrayList<String> = arrayListOf(),
@SerializedName("relationship_status"     ) var relationshipStatus  : String?           = null,
@SerializedName("email"                   ) var email               : String?           = null,
@SerializedName("password"                ) var password            : String?           = null,
@SerializedName("what_i_guessed"          ) var whatIGuessed        : String?           = null,
@SerializedName("what_i_want_to_tell_you" ) var whatIWantToTellYou  : String?           = null,
@SerializedName("following"               ) var following           : ArrayList<String> = arrayListOf(),
@SerializedName("followers"               ) var followers           : ArrayList<String> = arrayListOf(),
@SerializedName("is_member_plus_profile"  ) var isMemberPlusProfile : Boolean?          = null,
@SerializedName("_id"                     ) var Id                  : String?           = null,
@SerializedName("createdAt"               ) var createdAt           : String?           = null,
@SerializedName("updatedAt"               ) var updatedAt           : String?           = null,
@SerializedName("__v"                     ) var _v                  : Int?              = null
)
