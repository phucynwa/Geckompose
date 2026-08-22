package com.phucynwa.geckompose

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSession.SessionState
import org.mozilla.geckoview.GeckoSessionSettings
import org.mozilla.geckoview.GeckoView

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

        baseUrl?.let { loader.uri(it) }

        val finalMimeType = if (encoding != null) {
            "${mimeType ?: "text/html"}; charset=$encoding"
        } else {
            mimeType ?: "text/html"
        }

        loader.data(data, finalMimeType)

        session?.load(loader)
    }

    fun postUrl(url: String, postData: ByteArray) {
        val postString = String(postData, Charsets.UTF_8)
        val html = POST_FORM_HTML
            .trimIndent()
            .replace($$"$url", url)
            .replace($$"$postData", postString)
        loadDataWithBaseURL(url, html, "text/html", "utf-8", null)
    }

    fun restoreState(inState: Bundle) {
        val state: SessionState = inState.getParcelable(BUNDLE_KEY) ?: return
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

        private const val POST_FORM_HTML = $$"""
            <html>
            <head>
            <script>
            function post() {
                var form = document.createElement("form");
                form.method = "POST";
                form.action = "$url";
                var params = "$postData".split("&");
                for (var i = 0; i < params.length; i++) {
                    var pair = params[i].split("=");
                    if (pair.length >= 1) {
                        var input = document.createElement("input");
                        input.type = "hidden";
                        input.name = decodeURIComponent(pair[0]);
                        input.value = decodeURIComponent(pair[1] || "");
                        form.appendChild(input);
                    }
                }
                document.body.appendChild(form);
                form.submit();
            }
            </script>
            </head>
            <body onload="post()">
            </body>
            </html>
        """
    }
}
