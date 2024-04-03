package com.satguru.veritask.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.satguru.veritask.models.DeviceInfo
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

object Constants {
    const val DEFAULT_PASSWORD: String = "123456"
    const val NOTIFICATION_ID: String = "NOTIFICATION_ID"
    const val NO_ACTION: String = "NO_ACTION"
    const val PENDING = "Pending"
    const val APPROVED = "Approved"
    const val REJECTED = "Rejected"

    private val amountFormatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
        this.minimumFractionDigits = 2
        this.maximumFractionDigits = 2
    }

    private val defaultApiDateFormatter =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    private val outputDateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    private val outputTimeFormatter = SimpleDateFormat("h:mm a", Locale.US)

    fun formatAmount(amountString: String): String {
        return try {
            amountFormatter.format(amountString.toDouble())
        } catch (e: Exception) {
            amountString
        }
    }

    fun formatDate(inputDate: String): Pair<String, String> {
        return try {
            val date = defaultApiDateFormatter.parse(inputDate)!!
            val formattedDate = outputDateFormatter.format(date)
            val formattedTime = outputTimeFormatter.format(date).lowercase()
            Pair(formattedDate, formattedTime)
        } catch (e: Exception) {
            Pair(inputDate, "")
        }
    }

    fun createDeviceTokenRequestObject(
        context: Context,
        token: String,
        userId: String
    ): DeviceInfo {
        return DeviceInfo(
            os = "Android",
            token = token,
            deviceId = getDeviceId(context),
            userId = userId
        )
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}
