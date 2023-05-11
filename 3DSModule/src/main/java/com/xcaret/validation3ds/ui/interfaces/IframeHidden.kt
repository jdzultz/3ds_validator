package com.xcaret.validation3ds.ui.interfaces

import com.xcaret.validation3ds.domain.models.PostMessage

interface IframeHidden {
    fun onIframeFinished(data: PostMessage)
    fun onError()
}