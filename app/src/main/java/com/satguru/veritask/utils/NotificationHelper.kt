package com.satguru.veritask.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.satguru.veritask.DeeplinkActivity
import com.satguru.veritask.R

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

    fun sendNotification(context: Context, remoteMessage: RemoteMessage, notificationId: Int) {
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

        val notificationType = remoteMessage.data[EXTRA_TYPE].orEmpty()
        if (!SUPPORTED_TYPES.contains(notificationType)) {
            return
        }
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
            }

            "" -> {}
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        // Create Notification Channel only for Android 8.0 (Oreo) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
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