package com.xcaret.validation3ds.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(
        this
    ) {
        it?.let { t -> observer(t) }
    }
}

/*fun <T> LifecycleOwner.observe(liveData: MutableLiveData<T>, observer: (T) -> Unit) {
    liveData.observe(
        this
    ) {
        it?.let { t -> observer(t) }
    }
}*/