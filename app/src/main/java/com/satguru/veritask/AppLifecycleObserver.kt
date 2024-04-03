package com.satguru.veritask

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object AppLifecycleObserver : LifecycleEventObserver {

    private var isAppInForeground = false
    private val state = MutableSharedFlow<String>()

    fun isAppInForeground(): Boolean {
        return isAppInForeground
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateState(newValue: String) {
        if (!state.tryEmit(newValue)) {
            GlobalScope.launch {
                state.emit(newValue)
            }
        }
    }

    fun init() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            isAppInForeground = true
        } else if (event == Lifecycle.Event.ON_STOP) {
            isAppInForeground = false
        }
    }
}
