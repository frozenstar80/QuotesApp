package com.example.quotesapp.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Constants {
    const val USER_TYPE = "user_type"
    const val BASE_URL = "http://143.110.247.128:8008/"
    const val IMAGE_BASE_URL = "http://143.110.247.128:8008/user/image/"
    const val OTHER_USER_SOURCE = "search_fragment_source"
    const val OWN_USER_SOURCE = "home_fragment_source"
    const val ISLOGIN = "is_login"
    const val USER_ID = "id"
    const val USER = "user"
    const val LOCALE = "locale"
    const val TOKEN = "token"
    const val SEARCH_PROFILE_TIME_DELAY = 500L
    const val FULLNAME = "full_name"
    const val PREVNAME = "previous_name"
    const val WIFEHUSNAME = "wife_husband_name"
    const val NEWNAME = "new_name"
    const val DOB = "date_of_birth"
    const val DOD = "date_of_death"
    const val PHOTOS = "photos"
    const val MYFINALWORD = "final_words"
    const val WANTTOTELL = "what_i_want_to_tell_you"
    const val GUESSED = "what_i_guessed"
    const val QUOTATION = "quotations"
    const val RESIDENCE = "last_place_of_residence"
    const val RELATIONSHIPSTATUS = "relationship_status"
    const val DATEMONTHYEAR = "dd/MM/yyyy"
    const val MONTHDATEYEAR = "MM-dd-yyyy"
    const val EMAIL = "email"
    const val ACCEPT = "accept"
    const val REJECT = "reject"
    const val BURIAL = "burials"
    const val MOTIVATION = "motivations"
    const val LINKS = "links"
    const val IDEAS = "ideas"
    const val ITEM_TYPE = "item_type"
    const val IS_OTHER_PROFILE = "is_other_profile"
    const val TAB = "tab"
    const val BURIAL1 ="Procedure funeral"
    const val BURIAL2="lyrics, songs"
    const val BURIAL3 ="Obituary"
    const val BURIAL4="Information"
    const val BURIAL5="Burial (type/grave)"
    const val MOTIV1 ="Other cultures"
    const val MOTIV2="Life hacks"
    const val MOTIV3 ="Goal"
    const val MOTIV4="Sayings"
    const val MOTIV5="Stories"
    const val LINKS1 ="Funeral directors"
    const val LINKS2="Lawyers"
    const val LINKS3 ="Notaries"
    const val LINKS4="Support Heart"
    const val LINKS5="Condolence cards"
    const val LINKS6="First aid"
    const val IDEAS1 ="Ideas"
    const val IDEAS2="Account Details"
    const val IDEAS3 ="Payment Function"
    const val IDEAS4="Improvements"
    const val IDEAS5="Imprint"
    const val API_KEY = "Fm5XGDOb3h3sT2vZplVC8ZrJEYnguLf3MXKHDuiNELgFqjrAttgEoqgrnCoDv7bk1eKWQGoId1lTf2O7LrPrg50NDuuG3m29QY4BR8oIWjjr3QLL9oTUDoRB63Glrg4u"

    fun formatDate(date: String?, format: String?): String {
        if (date.isNullOrEmpty()) return ""
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat(format, Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = inputFormat.parse(date)
        return if (dateTime != null) {
            outputFormat.format(dateTime)
        } else ""
    }

    const val IMAGE = "image"
    const val VIDEO = "video"
    const val DOCUMENT = "document"
    const val QUOTE = "quote"
    const val QUOTE_ID = "quote_id"
    const val EMPTY = "empty"

    const val WRITE_PDF_REQUEST_CODE=123


}