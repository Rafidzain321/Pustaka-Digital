package com.example.pustaka.basic_api.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pustaka.R
import com.example.pustaka.databinding.ActivityPdfReaderBinding
import com.example.pustaka.databinding.ActivityReadPdfBinding


class ReadPdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadPdfBinding
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        url = intent.getStringExtra("URL").orEmpty()
        val googleDocsViewer = "https://docs.google.com/gview?embedded=true&url=$url"

        // Enable JavaScript and DOM Storage
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        // Set WebViewClient
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Show loading message when the page starts loading
                binding.loadingText.visibility = android.view.View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Hide loading message when the page finishes loading
                binding.webView.visibility = View.VISIBLE
                binding.loadingText.visibility = android.view.View.GONE
            }
        }

        // Load the URL
        binding.webView.loadUrl(googleDocsViewer)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
