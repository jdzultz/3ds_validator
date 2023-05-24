package com.xcaret.validation3ds.utils;

import android.content.Context

fun loadAndModifyIndexHtml(fileName: String, context: Context, modifications: Map<String, String>): String {
    val inputStream = context.assets.open(fileName)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    var indexHtml = String(buffer, Charsets.UTF_8)

    for ((key, value) in modifications) {
        indexHtml = indexHtml.replace(key, value)
    }

    return indexHtml
}