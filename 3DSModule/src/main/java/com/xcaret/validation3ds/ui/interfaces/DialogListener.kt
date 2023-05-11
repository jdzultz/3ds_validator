package com.xcaret.validation3ds.ui.interfaces

interface DialogListener {
    fun onPageStarted(url: String?)
    fun onPageFinished(url: String?)
    fun onReceivedError(errorCode: Int, description: String?, failingUrl: String?)
}