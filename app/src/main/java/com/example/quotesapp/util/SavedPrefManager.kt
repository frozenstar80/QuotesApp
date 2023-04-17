package com.example.quotesapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.quotesapp.util.Constants.FULLNAME
import com.example.quotesapp.util.Constants.ISLOGIN
import com.example.quotesapp.util.Constants.LOCALE
import com.example.quotesapp.util.Constants.PHOTOS
import com.example.quotesapp.util.Constants.TOKEN
import com.example.quotesapp.util.Constants.USER_ID


class
SavedPrefManager(context: Context?) {
    private val SHARED_PREF_NAME = "Quotes App"
    private var sharedPreferences: SharedPreferences? =
        context!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor? = sharedPreferences?.edit()

    fun isLogin(): Boolean{
        return sharedPreferences!!.getBoolean(ISLOGIN, false)
    }
    fun putLogin(isLogin : Boolean) {
        editor?.putBoolean(ISLOGIN, isLogin)
        editor?.apply()
    }
    fun getLocale(): String?{
        return sharedPreferences!!.getString(LOCALE, "en")
    }
    fun putLocale(locale : String) {
        editor?.putString(LOCALE, locale)
        editor?.apply()
    }
    fun getToken(): String?{
        return sharedPreferences!!.getString(TOKEN, "")
    }
    fun getFullName(): String?{
        return sharedPreferences!!.getString(FULLNAME, "")
    }
    fun getPhoto(): String?{
        return sharedPreferences!!.getString(PHOTOS, "")
    }
    fun getId(): String?{
        return sharedPreferences!!.getString(USER_ID, "")
    }

    fun putLoginDetails(token:String?,fullname:String?,photo: String?,userID:String?) {
        editor?.putString(FULLNAME,fullname)
        editor?.putString(PHOTOS,photo)
        editor?.putString(TOKEN,token)
        editor?.putString(USER_ID,userID)
        editor?.apply()
    }

    fun putNamePhotoDetails(fullname:String?,photo: String?) {
        editor?.putString(FULLNAME,fullname)
        editor?.putString(PHOTOS,photo)
        editor?.apply()
    }



}