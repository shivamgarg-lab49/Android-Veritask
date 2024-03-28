package com.satguru.veritask.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import retrofit2.Response

inline fun <reified ResponseType> networkCall(
    timeout: Long = 10000,
    showLoader: Boolean = true,
    crossinline fetch: suspend () -> Response<ResponseType>,
) = flow<UiState<ResponseType>> {
    try {
        if (showLoader) {
            emit(UiState.Loading)
        }
        withTimeout(timeout) {
            val response: Response<ResponseType> = fetch()
            if (response.isSuccessful && response.body() != null) {
                emit(UiState.Success(response.body()!!))
            } else {
                emit(UiState.Error(IllegalStateException(response.message())))
            }
        }
    } catch (exception: Throwable) {
        emit(UiState.Error(exception))
        if (exception is CancellationException) {
            throw exception
        }
    }
}