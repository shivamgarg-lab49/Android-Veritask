package com.satguru.veritask

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object SharedApp : LifecycleEventObserver {

    private var isAppInForeground = false
    val queue = MutableSharedFlow<BaseMessage>()

    fun isAppInForeground(): Boolean {
        return isAppInForeground
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun postMessage(newMessage: BaseMessage) {
        if (!queue.tryEmit(newMessage)) {
            GlobalScope.launch { queue.emit(newMessage) }
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
