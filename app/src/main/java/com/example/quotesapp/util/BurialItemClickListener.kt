package com.example.quotesapp.util

import com.example.quotesapp.data.BurialData

interface BurialItemClickListener {
    fun clicked(burialData: BurialData)
    fun clicked(name:String)
}