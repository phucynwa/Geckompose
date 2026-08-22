package com.phucynwa.geckompose

import android.content.Context
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings
import org.mozilla.geckoview.WebRequestError

open class GeckoSessionClient private constructor(
    settings: GeckoSessionSettings? = null
) : GeckoSession(settings) {

    internal var webViewClient: WebViewClient? = null
    internal var webChromeClient: WebChromeClient? = null
    internal var currentState: SessionState? = null

    internal inner class InnerNavigationDelegate(
        private val webView: WebView
    ) : NavigationDelegate {

        override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
            super.onCanGoBack(session, canGoBack)
            webView.canGoBack = canGoBack
        }

        override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
            super.onCanGoForward(session, canGoForward)
            webView.canGoForward = canGoForward
        }

        override fun onLoadError(
            session: GeckoSession,
            uri: String?,
            error: WebRequestError
        ): GeckoResult<String>? {
            webViewClient?.onReceivedError(view = webView, request = null, error = error)
            return super.onLoadError(session, uri, error)
        }
    }

    internal inner class InnerContentDelegate(
        private val webView: WebView
    ) : ContentDelegate {

        override fun onTitleChange(session: GeckoSession, title: String?) {
            super.onTitleChange(session, title)
            webChromeClient?.onReceivedTitle(webView, title)
        }
    }

    internal inner class InnerProgressDelegate(
        private val webView: WebView
    ) : ProgressDelegate {

        override fun onPageStart(session: GeckoSession, url: String) {
            super.onPageStart(session, url)
            webViewClient?.onPageStarted(webView, url, null)
        }

        override fun onPageStop(session: GeckoSession, success: Boolean) {
            super.onPageStop(session, success)
            webViewClient?.onPageFinished(webView, null)
        }

        override fun onProgressChange(session: GeckoSession, progress: Int) {
            super.onProgressChange(session, progress)
            webChromeClient?.onProgressChanged(webView, progress)
        }

        override fun onSessionStateChange(session: GeckoSession, sessionState: SessionState) {
            super.onSessionStateChange(session, sessionState)
            currentState = sessionState
        }
    }

    internal inner class InnerHistoryDelegate(
        private val webView: WebView
    ) : HistoryDelegate {

        override fun onVisited(
            session: GeckoSession,
            url: String,
            lastVisitedURL: String?,
            flags: Int
        ): GeckoResult<Boolean>? {
            val isReload = url == lastVisitedURL
            webViewClient?.doUpdateVisitedHistory(webView, url, isReload)
            return super.onVisited(session, url, lastVisitedURL, flags)
        }
    }

    class Builder(context: Context) {

        private val runtime by lazy {
            GeckoFactory.createGeckoRuntime(context)
        }
        private var _webView: WebView? = null
        private var _settings: GeckoSessionSettings? = null

        fun setWebView(webView: WebView) = apply {
            _webView = webView
        }

        fun setSettings(settings: GeckoSessionSettings) = apply {
            _settings = settings
        }

        fun build() = GeckoSessionClient(_settings).apply {
            _webView?.let { webView ->
                contentDelegate = InnerContentDelegate(webView)
                navigationDelegate = InnerNavigationDelegate(webView)
                progressDelegate = InnerProgressDelegate(webView)
                historyDelegate = InnerHistoryDelegate(webView)
            }
            open(runtime)
        }
    }
}
