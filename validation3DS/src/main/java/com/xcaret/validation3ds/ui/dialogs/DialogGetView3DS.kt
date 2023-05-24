package com.xcaret.validation3ds.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.xcaret.validation3ds.ui.interfaces.IframeShow
import com.xcaret.validation3ds.ui.interfaces.ViewShowJavascriptInterface
import com.xcaret.validation3ds.utils.loadAndModifyIndexHtml
import java.net.URLEncoder
import kotlin.math.roundToInt


class DialogGetView3DS(context: Context, private val listener: IframeShow) :
    Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen) {
    private var callbackOnLoadIframe: (() -> Unit)? = null
    private var temporalCountIntentLoadWeb: Int = 0
    private val MAX_INTENTS: Int = 3
    private var webView = WebView(context)

    private val myJavascriptInterface = object : ViewShowJavascriptInterface {
        @JavascriptInterface
        override fun onLoadIframe() {

            Log.e("load iframe", "---dialog 3ds ${temporalCountIntentLoadWeb}")
            temporalCountIntentLoadWeb++
            if (temporalCountIntentLoadWeb == MAX_INTENTS) {
                listener.onIframeFinished()
                callbackOnLoadIframe?.invoke()
                cleanTemporalVariable()
            }
        }
    }

    private fun cleanTemporalVariable () {
        temporalCountIntentLoadWeb = 0
        dismiss()
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

    fun show(mdParam: String, jwtParam: String, urlForm: String, title:String? = null, subtitle:String? = null, callback: (() -> Unit)? = null) {
        this.callbackOnLoadIframe = callback

        val modificationsForm = mapOf(
            "#MD" to mdParam,
            "#JWT" to jwtParam,
            "#ACTION" to urlForm,
        )
        val formHtml = loadAndModifyIndexHtml("form_show.html",context,modificationsForm);

        var modificationsIndex = mapOf(
            "#FORM" to  "data:text/html;charset=utf-8," + URLEncoder.encode(formHtml, "UTF-8").replace("+", "%20"),
        )

        if(title != null) {
            modificationsIndex = modificationsIndex + ("#TITLE" to title!!)
        }
        if(subtitle != null) {
            modificationsIndex = modificationsIndex + ("#SUBTITLE" to subtitle!!)
        }

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

        val displayMetrics = context.resources.displayMetrics
        val height = displayMetrics.heightPixels
        val webViewHeight = (height * 0.73).roundToInt()

        webView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            webViewHeight
        )

        val layoutParams = webView.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.CENTER

        layoutParams.setMargins(20, 0, 20, 0) // Ajustar los márgenes izquierdo y derecho

        webView.layoutParams = layoutParams

        webView.background = ColorDrawable(Color.TRANSPARENT)
        webView.clipToOutline = true
        webView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                //Establece el borde redondeado para el WebView
                outline?.setRoundRect(0, 0, view!!.width, view.height, 30f)
            }
        }
        /*webView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )*/
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
        (window?.decorView as FrameLayout).addView(webView)
        setCancelable(false)
    }
}