package com.phucynwa.geckompose

import android.content.Context
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

internal object GeckoFactory {

    @Volatile
    private var geckoRuntime: GeckoRuntime? = null

    internal fun createGeckoRuntime(context: Context): GeckoRuntime {
        return geckoRuntime ?: synchronized(this) {
            geckoRuntime ?: GeckoRuntime.create(context.applicationContext).also {
                geckoRuntime = it
            }
        }
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
