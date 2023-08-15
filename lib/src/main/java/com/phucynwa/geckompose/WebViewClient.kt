package com.phucynwa.geckompose

import android.graphics.Bitmap

open class WebViewClient {

    open fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {}

    open fun onPageFinished(view: WebView, url: String?) {}

    open fun doUpdateVisitedHistory(view: WebView, url: String?, reload: Boolean) {}

    open fun onReceivedError(
        view: WebView,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
    }
}
