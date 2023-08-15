package com.phucynwa.geckompose

import android.content.Context
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

internal object GeckoFactory {

    private lateinit var geckoRuntime: GeckoRuntime

    internal fun createGeckoRuntime(context: Context): GeckoRuntime {
        if (this::geckoRuntime.isInitialized.not()) {
            geckoRuntime = GeckoRuntime.create(context)
        }
        return geckoRuntime
    }

    internal fun createGeckoSession(context: Context): GeckoSession {
        val session = GeckoSession()
        session.contentDelegate = object : GeckoSession.ContentDelegate {}
        session.open(createGeckoRuntime(context))
        return session
    }

    internal fun createGeckoSessionClient(context: Context): GeckoSessionClient {
        return GeckoSessionClient.Builder(context).build()
    }
}
