package com.satguru.veritask

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object SharedApp : LifecycleEventObserver {

    private var isAppInForeground = false

    private val _queue = MutableSharedFlow<BaseMessage>()
    val queue = _queue.asSharedFlow()

    fun isAppInForeground(): Boolean {
        return isAppInForeground
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun postMessage(newMessage: BaseMessage) {
        if (!_queue.tryEmit(newMessage)) {
            GlobalScope.launch { _queue.emit(newMessage) }
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
