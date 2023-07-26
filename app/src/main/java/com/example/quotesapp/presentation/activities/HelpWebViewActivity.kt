package com.example.quotesapp.presentation.activities

import android.app.Activity
import android.os.Bundle
import com.example.quotesapp.databinding.FragmentHelpWebViewBinding
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.WebViewClientImpl


class HelpWebViewActivity : Activity() {

    lateinit var binding: FragmentHelpWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  FragmentHelpWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data  =  intent.getStringExtra(Constants.DOCUMENT)

        binding.webView.settings.apply {
            javaScriptEnabled  =true
            useWideViewPort = true
            loadWithOverviewMode = true
            domStorageEnabled = true
        }
        val client =  WebViewClientImpl()
        binding.webView.webViewClient =  client

        if (data != null) {
            binding.webView.loadUrl(data)
        }

    }

        override fun onBackPressed() {
        if(binding.webView.canGoBack())
        {
            binding.webView.goBack()
        }
        else
        {
            super.onBackPressed()
        }
    }
}