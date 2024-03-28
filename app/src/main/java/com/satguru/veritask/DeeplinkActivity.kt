package com.satguru.veritask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class DeeplinkActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        intent.data = getIntent().data
        startActivity(intent)

        finish()
    }
}