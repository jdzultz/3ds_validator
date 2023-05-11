package com.xcaret.validation3ds.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import com.xcaret.validation3ds.domain.models.PostMessage
import com.xcaret.validation3ds.ui.interfaces.IframeHidden
import com.xcaret.validation3ds.ui.interfaces.ViewHiddenJavascriptInterface
import com.xcaret.validation3ds.utils.loadAndModifyIndexHtml
import org.json.JSONObject
import java.net.URLEncoder


class DialogGetSessionId(context: Context, private val listener: IframeHidden) :
    Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen) {

    private var callbackOnLoadIframe: ((data: PostMessage?) -> Unit)? = null

    private var temporalPostMessage: PostMessage? = null
    private var temporalCountIntentLoadWeb: Int = 0
    private val MAX_INTENTS: Int = 2

    private var binParam: String? = null
    private var jwtParam: String? = null
    private var webView = WebView(context)

    private val myJavascriptInterface = object : ViewHiddenJavascriptInterface {
        @JavascriptInterface
        override fun onMessageReceived(data: String) {
            val json = JSONObject(data)
            val messageType = json.getString("MessageType")
            val sessionId = json.getString("SessionId")
            val status = json.getBoolean("Status")

            temporalPostMessage = PostMessage(
                messageType = messageType,
                sessionId = sessionId,
                status = status
            )
        }
        @JavascriptInterface
        override fun onLoadIframe() {
            Log.e("load iframe", "---")
            temporalCountIntentLoadWeb++
            if (temporalCountIntentLoadWeb == MAX_INTENTS) {
                if (temporalPostMessage != null){
                    listener.onIframeFinished(temporalPostMessage!!)
                    callbackOnLoadIframe?.invoke(temporalPostMessage)
                    dismiss()
                    ///webView.destroy()
                }else {
                    Log.e("ERRROOORRRRR","no generaro" )
                    listener.onError()
                    callbackOnLoadIframe?.invoke(temporalPostMessage)
                    dismiss()
                    ///webView.destroy()
                }
                cleanTemporalVariable()
            }
        }
    }

    private fun cleanTemporalVariable () {
        temporalPostMessage = null
        temporalCountIntentLoadWeb = 0
    }

    private fun injectJavaScript(view: WebView?) {
        //inject js in webview page
        view!!.loadUrl(
            """
            javascript:window.addEventListener('message', function(event) {
                Android.onMessageReceived(event.data);
            });
            
            const iframe = document.getElementById('myIframe');
            iframe.onload = function() {
                Android.onLoadIframe();
            };
        """
        )
    }

    fun show(binParam: String, jwtParam: String, callback: ((data: PostMessage?) -> Unit)? = null) {
        this.callbackOnLoadIframe = callback
        this.binParam = binParam
        this.jwtParam = jwtParam

        val modificationsForm = mapOf(
            "#BIN" to binParam,
            "#JWT" to jwtParam
        )
        val formHtml = loadAndModifyIndexHtml("form_hidden.html",context,modificationsForm);

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