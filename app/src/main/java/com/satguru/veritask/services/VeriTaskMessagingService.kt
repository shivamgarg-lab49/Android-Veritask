package com.satguru.veritask.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.satguru.veritask.di.RepositoryService
import com.satguru.veritask.utils.Constants
import com.satguru.veritask.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject


@AndroidEntryPoint
@DelicateCoroutinesApi
class VeriTaskMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var repositoryService: RepositoryService

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if (repositoryService.getSharedPreference().isLoggedIn()) {
            val loggedInUser = repositoryService.getSharedPreference().requireLoggedInUser()
            val info = Constants.createDeviceTokenRequestObject(
                context = this@VeriTaskMessagingService,
                token = token,
                userId = loggedInUser.id
            )
            repositoryService.login(info).launchIn(GlobalScope)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val gson = Gson()
        val gsonType = object : TypeToken<Any?>() {}.type
        val gsonString: String = gson.toJson(remoteMessage.data, gsonType)
        println("onMessageReceived--->>>   $gsonString")
        val user = repositoryService.getSharedPreference().getLoggedInUser()
        if (user != null && user.id.isNotEmpty()) {
            NotificationHelper.sendNotification(
                context = this,
                remoteMessage = remoteMessage,
            )
        }
    }
}