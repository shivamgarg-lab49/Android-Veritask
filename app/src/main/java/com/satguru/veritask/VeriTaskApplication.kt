package com.satguru.veritask

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VeriTaskApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedApp.init()
    }
}