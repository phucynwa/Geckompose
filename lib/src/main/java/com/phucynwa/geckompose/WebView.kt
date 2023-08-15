package com.phucynwa.geckompose

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.GeckoWebExecutor
import org.mozilla.geckoview.WebRequest
import java.nio.ByteBuffer

class WebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : GeckoView(context, attrs) {

    internal var canGoBack = false
    internal var canGoForward = false

    val settings: GeckoSessionSettings? get() = session?.settings

    internal var webChromeClient: WebChromeClient?
        get() = session?.webChromeClient
        set(value) {
            session?.webChromeClient = value
        }
    internal var webViewClient: WebViewClient?
        get() = session?.webViewClient
        set(value) {
            session?.webViewClient = value
        }

    init {
        isSaveEnabled = true
        setupGeckoSession()
    }

    private fun setupGeckoSession() {
        val geckoSessionSettings = GeckoSessionSettings.Builder()
            .userAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE)
            .viewportMode(GeckoSessionSettings.VIEWPORT_MODE_MOBILE)
            .build()
        val geckoSession = GeckoSessionClient.Builder(context)
            .setWebView(this)
            .setSettings(geckoSessionSettings)
            .build()
        releaseSession()
        setSession(geckoSession)
    }

    override fun getSession(): GeckoSessionClient? {
        return mSession as? GeckoSessionClient?
    }

    fun goBack() {
        session?.goBack()
    }

    fun goForward() {
        session?.goForward()
    }

    fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {
        val loader = GeckoSession.Loader()
            .additionalHeaders(additionalHttpHeaders)
            .uri(url)
        session?.load(loader)
    }

    fun loadDataWithBaseURL(
        baseUrl: String?,
        data: String,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?,
    ) {
        val loader = GeckoSession.Loader()
            .data(data, "text/html")
        session?.load(loader)
    }

    fun postUrl(url: String, postData: ByteArray) {
        val webRequest = WebRequest.Builder(url)
            .method("POST")
            .body(ByteBuffer.wrap(postData))
            .build()
        GeckoWebExecutor(GeckoFactory.createGeckoRuntime(context)).fetch(webRequest)
    }

    fun restoreState(inState: Bundle) {
        val state: GeckoSession.SessionState = inState.getParcelable(BUNDLE_KEY) ?: return
        session?.restoreState(state)
    }

    fun canGoBack(): Boolean {
        return canGoBack
    }

    fun canGoForward(): Boolean {
        return canGoForward
    }

    fun saveState(bundle: Bundle) {
        bundle.putParcelable(BUNDLE_KEY, session?.currentState)
    }

    companion object {

        private const val BUNDLE_KEY = "gecko"
    }
}
