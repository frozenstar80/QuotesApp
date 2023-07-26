package com.example.quotesapp.util

import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewClientImpl() : WebViewClient() {



    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.shouldOverrideUrlLoading(view, url)",
        "android.webkit.WebViewClient"
    )
    )
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null) {
            view?.loadUrl(url)
        }
        return true
    }

}