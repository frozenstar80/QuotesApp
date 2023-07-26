package com.example.quotesapp.util

import com.example.quotesapp.data.FileData

interface HomeItemClickListener {
    fun clicked(id:String,type:String)
    fun clicked(type: String)
    fun openEditPage(fileData: FileData)
}