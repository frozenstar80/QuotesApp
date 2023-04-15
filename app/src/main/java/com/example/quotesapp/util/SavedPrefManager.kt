package com.example.quotesapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.quotesapp.util.Constants.ISLOGIN


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


}