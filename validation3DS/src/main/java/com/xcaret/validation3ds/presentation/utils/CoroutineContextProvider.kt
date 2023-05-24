package com.xcaret.validation3ds.presentation.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineContextProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}

class CoroutineContextProviderImp constructor() : CoroutineContextProvider {
    override val io = Dispatchers.IO
    override val main: CoroutineDispatcher = Dispatchers.Main
}