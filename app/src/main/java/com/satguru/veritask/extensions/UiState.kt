package com.satguru.veritask.extensions

open class UiState<out T> {
    object Ideal : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val error: Throwable) : UiState<Nothing>()
}