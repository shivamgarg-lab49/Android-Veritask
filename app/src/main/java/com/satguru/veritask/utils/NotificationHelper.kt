package com.satguru.veritask.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.satguru.veritask.BaseMessage
import com.satguru.veritask.DeeplinkActivity
import com.satguru.veritask.R
import com.satguru.veritask.SharedApp
import com.satguru.veritask.models.User


object NotificationHelper {

    private const val EXTRA_TITLE = "title"
    private const val EXTRA_MESSAGE = "message"
    private const val EXTRA_TYPE = "type"
    private const val EXTRA_DEAL_ID = "dealId"


    private const val TYPE_DEAL_CREATED = "DealCreated"
    private val SUPPORTED_TYPES = listOf(TYPE_DEAL_CREATED)


    private const val CHANNEL_ID = "default"
    private const val CHANNEL_NAME = "Deal"
    private const val CHANNEL_DESCRIPTION = "Deals creation notification"

    fun sendNotification(
        context: Context,
        remoteMessage: RemoteMessage,
        notificationId: Int,
        loggedInUser: User?
    ) {
        val gson = Gson()
        val gsonType = object : TypeToken<Any?>() {}.type
        val gsonString: String = gson.toJson(remoteMessage.data, gsonType)
        println("onMessageReceived--------------------------------------------")
        println("onMessageReceived--->>>   $gsonString")

        val notificationType = remoteMessage.data[EXTRA_TYPE].orEmpty()
        if (!areNotificationOn(context)
            || !SUPPORTED_TYPES.contains(notificationType)
            || loggedInUser?.id.isNullOrBlank()
        ) {
            println("onMessageReceived--->>>  check failed")
            println("onMessageReceived--------------------------------------------")
            return
        }
        println("onMessageReceived--->>>  check passed, about to show notification")
        println("onMessageReceived--------------------------------------------")

        // create notification channel
        createNotificationChannel(context)

        // setup title and message
        val title = remoteMessage.data[EXTRA_TITLE].orEmpty()
        val message = remoteMessage.data[EXTRA_MESSAGE].orEmpty()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        when (notificationType) {
            TYPE_DEAL_CREATED -> {
                val dealId = remoteMessage.data[EXTRA_DEAL_ID].orEmpty()
                builder.setContentIntent(
                    getDealDetailIntent(
                        context,
                        dealId,
                        Constants.NO_ACTION,
                        notificationId
                    )
                )
                builder.addAction(
                    0,
                    context.getString(R.string.approve),
                    getDealDetailIntent(context, dealId, Constants.APPROVED, notificationId)
                )
                builder.addAction(
                    0,
                    context.getString(R.string.reject_with_comments),
                    getDealDetailIntent(context, dealId, Constants.REJECTED, notificationId)
                )
                SharedApp.postMessage(BaseMessage.DealCreatedMessage(dealId = dealId))
            }

            "" -> {}
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    private fun areNotificationOn(context: Context): Boolean {
        val permissionState =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            } else {
                PackageManager.PERMISSION_GRANTED
            }
        val permissionGranted = permissionState == PackageManager.PERMISSION_GRANTED
        val isChannelOn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationManagerCompat.from(context).getNotificationChannel(CHANNEL_ID)
            channel != null && channel.importance != NotificationManager.IMPORTANCE_NONE
        } else {
            true
        }
        return permissionGranted && isChannelOn
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = CHANNEL_DESCRIPTION
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    private fun getDealDetailIntent(
        context: Context,
        dealId: String,
        action: String,
        notificationId: Int
    ): PendingIntent {
        val intent = Intent(
            context,
            DeeplinkActivity::class.java
        )

        intent.putExtra(Constants.NOTIFICATION_ID, notificationId)
        intent.data = Uri.parse(DeepLinkBuilder.createDealsDetailDeepLink(dealId, action))
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}