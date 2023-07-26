package com.example.quotesapp.data

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<DataNotification> = arrayListOf()

)
data class ToUser (

    @SerializedName("_id"                       ) var Id                     : String?           = null,
    @SerializedName("full_name"                 ) var fullName               : String?           = null,
    @SerializedName("previous_name"             ) var previousName           : String?           = null,
    @SerializedName("new_name"                  ) var newName                : String?           = null,
    @SerializedName("wife_husband_name"         ) var wifeHusbandName        : String?           = null,
    @SerializedName("quotations"                ) var quotations             : String?           = null,
    @SerializedName("date_of_birth"             ) var dateOfBirth            : String?           = null,
    @SerializedName("date_of_death"             ) var dateOfDeath            : String?           = null,
    @SerializedName("last_place_of_residence"   ) var lastPlaceOfResidence   : String?           = null,
    @SerializedName("photos"                    ) var photos                 : ArrayList<String> = arrayListOf(),
    @SerializedName("documents"                 ) var documents              : ArrayList<String> = arrayListOf(),
    @SerializedName("relationship_status"       ) var relationshipStatus     : String?           = null,
    @SerializedName("email"                     ) var email                  : String?           = null,
    @SerializedName("password"                  ) var password               : String?           = null,
    @SerializedName("what_i_guessed"            ) var whatIGuessed           : String?           = null,
    @SerializedName("what_i_want_to_tell_you"   ) var whatIWantToTellYou     : String?           = null,
    @SerializedName("final_words"               ) var finalWords             : String?           = null,
    @SerializedName("following"                 ) var following              : ArrayList<String> = arrayListOf(),
    @SerializedName("followers"                 ) var followers              : ArrayList<String> = arrayListOf(),
    @SerializedName("forget_password_link_date" ) var forgetPasswordLinkDate : String?           = null,
    @SerializedName("is_member_plus_profile"    ) var isMemberPlusProfile    : Boolean?          = null,
    @SerializedName("createdAt"                 ) var createdAt              : String?           = null,
    @SerializedName("updatedAt"                 ) var updatedAt              : String?           = null,
    @SerializedName("__v"                       ) var _v                     : Int?              = null

)

data class FromUser (

    @SerializedName("_id"                       ) var Id                     : String?           = null,
    @SerializedName("full_name"                 ) var fullName               : String?           = null,
    @SerializedName("previous_name"             ) var previousName           : String?           = null,
    @SerializedName("new_name"                  ) var newName                : String?           = null,
    @SerializedName("wife_husband_name"         ) var wifeHusbandName        : String?           = null,
    @SerializedName("quotations"                ) var quotations             : String?           = null,
    @SerializedName("date_of_birth"             ) var dateOfBirth            : String?           = null,
    @SerializedName("date_of_death"             ) var dateOfDeath            : String?           = null,
    @SerializedName("last_place_of_residence"   ) var lastPlaceOfResidence   : String?           = null,
    @SerializedName("photos"                    ) var photos                 : ArrayList<String> = arrayListOf(),
    @SerializedName("documents"                 ) var documents              : ArrayList<String> = arrayListOf(),
    @SerializedName("relationship_status"       ) var relationshipStatus     : String?           = null,
    @SerializedName("email"                     ) var email                  : String?           = null,
    @SerializedName("password"                  ) var password               : String?           = null,
    @SerializedName("what_i_guessed"            ) var whatIGuessed           : String?           = null,
    @SerializedName("what_i_want_to_tell_you"   ) var whatIWantToTellYou     : String?           = null,
    @SerializedName("final_words"               ) var finalWords             : String?           = null,
    @SerializedName("following"                 ) var following              : ArrayList<String> = arrayListOf(),
    @SerializedName("followers"                 ) var followers              : ArrayList<String> = arrayListOf(),
    @SerializedName("forget_password_link_date" ) var forgetPasswordLinkDate : String?           = null,
    @SerializedName("is_member_plus_profile"    ) var isMemberPlusProfile    : Boolean?          = null,
    @SerializedName("createdAt"                 ) var createdAt              : String?           = null,
    @SerializedName("updatedAt"                 ) var updatedAt              : String?           = null,
    @SerializedName("__v"                       ) var _v                     : Int?              = null

)

data class DataNotification (
    @SerializedName("_id"                ) var Id                : String?   = null,
    @SerializedName("state" ) var notificationState : String?   = null,
    @SerializedName("is_approved"        ) var isApproved        : Boolean?  = null,
    @SerializedName("createdAt"          ) var createdAt         : String?   = null,
    @SerializedName("updatedAt"          ) var updatedAt         : String?   = null,
    @SerializedName("__v"                ) var _v                : Int?      = null,
    @SerializedName("to_user"            ) var toUser            : ToUser?   = ToUser(),
    @SerializedName("from_user"          ) var fromUser          : FromUser? = FromUser()

)
