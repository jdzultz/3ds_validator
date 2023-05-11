package com.xcaret.validation3ds.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.xcaret.validation3ds.ui.interfaces.IframeShow
import com.xcaret.validation3ds.ui.interfaces.ViewShowJavascriptInterface
import com.xcaret.validation3ds.utils.loadAndModifyIndexHtml
import java.net.URLEncoder


class DialogGetView3DS(context: Context, private val listener: IframeShow) :
    Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen) {
    private var callbackOnLoadIframe: (() -> Unit)? = null
    private var temporalCountIntentLoadWeb: Int = 0
    private val MAX_INTENTS: Int = 2

    private var mdParam: String? = null
    private var jwtParam: String? = null
    private var webView = WebView(context)

    private val myJavascriptInterface = object : ViewShowJavascriptInterface {
        @JavascriptInterface
        override fun onLoadIframe() {
            temporalCountIntentLoadWeb++

            if (temporalCountIntentLoadWeb >= MAX_INTENTS) {
                listener.onIframeFinished()
                callbackOnLoadIframe?.invoke()
                cleanTemporalVariable()
            }
        }
    }

    private fun cleanTemporalVariable () {
        temporalCountIntentLoadWeb = 0
        dismiss()
        webView.destroy()
    }

    private fun injectJavaScript(view: WebView?) {
        //inject js in webview page
        view!!.loadUrl(
            """     
            javascript:window.addEventListener('message', function(event) {
            });
            const iframe = document.getElementById('myIframe');
            iframe.onload = function() {
                Android.onLoadIframe();
            };
        """
        )
    }

    fun show(mdParam: String, jwtParam: String, callback: (() -> Unit)? = null) {
        this.callbackOnLoadIframe = callback
        this.mdParam = mdParam
        this.jwtParam = jwtParam

        val modificationsForm = mapOf(
            "#MD" to mdParam,
            "#JWT" to jwtParam
        )
        val formHtml = loadAndModifyIndexHtml("form_show.html",context,modificationsForm);

        val modificationsIndex = mapOf(
            "#FORM" to  "data:text/html;charset=utf-8," + URLEncoder.encode(formHtml, "UTF-8").replace("+", "%20"),
        )
        val indexHtml = loadAndModifyIndexHtml("index.html",context,modificationsIndex)

        webView.addJavascriptInterface(myJavascriptInterface, "Android")
        webView.loadDataWithBaseURL(null, indexHtml, "text/html", "UTF-8", null)

        super.show()
    }

    override fun show() {
        //obligamos al usuario que implemente el show siempre con los params necesario
        throw IllegalArgumentException("You must call show(params) to display this dialog")
    }

    init {
        setContentView(FrameLayout(context))
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        webView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {}
            override fun onPageFinished(view: WebView?, url: String?) {
                injectJavaScript(view)
            }
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                Log.e("error", errorCode.toString())
            }
        }

        // Agrega el WebView como contenido del Dialog.
        (window?.decorView as FrameLayout).addView(webView)
        setCancelable(false)
    }
}