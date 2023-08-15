package com.phucynwa.geckompose

import android.graphics.Bitmap

abstract class WebChromeClient {

    open fun onReceivedTitle(view: WebView, title: String?) {}

    open fun onReceivedIcon(view: WebView, icon: Bitmap?) {}

    open fun onProgressChanged(view: WebView, newProgress: Int) {}
}
