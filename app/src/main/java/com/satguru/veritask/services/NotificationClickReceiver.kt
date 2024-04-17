package com.satguru.veritask.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.satguru.veritask.BaseMessage
import com.satguru.veritask.R
import com.satguru.veritask.SharedApp
import com.satguru.veritask.di.RepositoryService
import com.satguru.veritask.extensions.toast
import com.satguru.veritask.models.DealApproveRequest
import com.satguru.veritask.utils.Constants
import com.satguru.veritask.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class NotificationClickReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repositoryService: RepositoryService

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(
            TAG,
            "Notification tapped: ${intent?.data.toString()}"
        )
        intent?.data?.also { uri ->
            NotificationHelper.dismissNotification(context, intent)
            uri.pathSegments.elementAtOrNull(uri.pathSegments.size - 2)?.also { dealId ->
                val loggedInUser = repositoryService.getSharedPreference().getLoggedInUser()
                if (loggedInUser != null) {
                    val body = DealApproveRequest(
                        approverId = loggedInUser.id,
                        status = Constants.APPROVED
                    )
                    repositoryService.approveDeal(dealId, body).onEach {
                        when (it) {
                            is com.satguru.veritask.extensions.UiState.Loading -> {
                                toast(context, "Approving...")
                            }

                            is com.satguru.veritask.extensions.UiState.Error -> {
                                toast(context, it.error.message.toString())
                            }

                            is com.satguru.veritask.extensions.UiState.Success -> {
                                toast(context, context.getString(R.string.successfully_approved))
                                SharedApp.postMessage(BaseMessage.DealCreatedMessage(dealId = dealId))
                            }
                        }
                    }.launchIn(GlobalScope)
                }
            }
        }
    }

    private fun toast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post { context.toast(message) }
    }

    companion object {
        private const val TAG = "NotificationClickReceive"
    }
}

