package com.satguru.veritask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.satguru.veritask.utils.NotificationHelper

class DeeplinkActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deepLinkActivityIntent = intent

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.data = deepLinkActivityIntent.data
        startActivity(mainActivityIntent)
        finish()

        NotificationHelper.dismissNotification(this, deepLinkActivityIntent)
    }
}