package com.example.quotesapp.util

import android.content.Context
import android.content.SharedPreferences


class
SavedPrefManager(context: Context?) {
    private val SHARED_PREF_NAME = "Quotes App"
    private var sharedPreferences: SharedPreferences? =
        context!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor? = sharedPreferences?.edit()



}