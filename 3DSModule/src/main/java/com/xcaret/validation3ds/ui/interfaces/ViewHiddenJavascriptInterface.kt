package com.xcaret.validation3ds.ui.interfaces

import android.webkit.JavascriptInterface

interface ViewHiddenJavascriptInterface {
    @JavascriptInterface
    fun onMessageReceived(message: String)
    @JavascriptInterface
    fun onLoadIframe()
}