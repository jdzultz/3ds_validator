package com.xcaret.validation3ds.domain.models


data class PostMessage(
    val messageType: String?,
    val sessionId: String?,
    val status: Boolean
)