package com.mdq.social.ui.terms

import android.R
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.mdq.social.databinding.ActivityTermsAndConditionBinding


class TermsAndCondition : AppCompatActivity() {

    var webview:WebView? =null
    var activityTermsAndConditionBinding:ActivityTermsAndConditionBinding ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTermsAndConditionBinding= ActivityTermsAndConditionBinding.inflate(layoutInflater);
        setContentView(activityTermsAndConditionBinding!!.root)

        val myWebView = findViewById<View>(com.mdq.social.R.id.webView) as WebView
        myWebView.loadUrl("http://beppers.in/Privacy.html")
        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true

        activityTermsAndConditionBinding!!.imageView.setOnClickListener {
            onBackPressed();
        }
    }
}