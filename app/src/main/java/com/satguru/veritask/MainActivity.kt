@file:OptIn(ExperimentalMaterialApi::class)

package com.satguru.veritask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.satguru.veritask.ui.features.NavGraphs
import com.satguru.veritask.ui.features.destinations.SalesDestination
import com.satguru.veritask.ui.features.destinations.SalesDetailsDestination
import com.satguru.veritask.ui.features.destinations.UsersScreenDestination
import com.satguru.veritask.ui.features.details.vm.SalesDetailViewModel
import com.satguru.veritask.ui.features.sales.vm.SalesViewModel
import com.satguru.veritask.ui.features.users.vm.UsersViewModel
import com.satguru.veritask.ui.theme.VeriTaskTheme
import com.satguru.veritask.ui.theme.fcl_fill_container
import com.satguru.veritask.utils.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            VeriTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.fcl_fill_container
                ) {
                    val state =
                        rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
                    if (!state.status.isGranted) {
                        LaunchedEffect(key1 = Unit, block = { state.launchPermissionRequest() })
                    }
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController!!,
                        dependenciesContainerBuilder = {
                            dependency(UsersScreenDestination) { hiltViewModel<UsersViewModel>() }
                            dependency(SalesDestination) { hiltViewModel<SalesViewModel>() }
                            dependency(SalesDetailsDestination) { hiltViewModel<SalesDetailViewModel>() }
                        })
                }
            }
        }
        handleDeepLink(navController, intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(navController, intent)
    }

    private fun handleDeepLink(navController: NavHostController?, intent: Intent?) {
        if (navController != null && sharedPreferences.isLoggedIn()) {
            navController.handleDeepLink(intent)
        }
    }
}