package com.xcaret.validation3ds.presentation.utils

interface UiModel;

open class UiAwareModel: UiModel {
    var isRedelivered: Boolean = false
}