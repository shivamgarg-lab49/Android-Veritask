package com.satguru.veritask.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
 fun <T> Flow<T>.handleErrors(action: suspend FlowCollector<T>.(cause: Throwable?) -> Unit): Flow<T> =
    flow {
        try {
            collect { value -> emit(value) }
        } catch (e: Throwable) {
            action(e)
        }
    }