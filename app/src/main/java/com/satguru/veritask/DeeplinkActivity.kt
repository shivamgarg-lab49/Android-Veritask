package com.satguru.veritask

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.satguru.veritask.utils.Constants

class DeeplinkActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        clearNotificationIfExist(getIntent())

        intent.data = getIntent().data
        startActivity(intent)

        finish()
    }

    private fun clearNotificationIfExist(intent: Intent) {
        val notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0)
        if (notificationId > 0) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.cancel(notificationId)
        }
    }
}