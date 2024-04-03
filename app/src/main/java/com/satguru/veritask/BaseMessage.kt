package com.satguru.veritask

import androidx.annotation.Keep

@Keep
sealed class BaseMessage {
    @Keep
    data class DealCreatedMessage(val dealId: String) : BaseMessage()
}