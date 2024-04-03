package com.satguru.veritask

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.satguru.veritask.di.RepositoryService
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.models.DeviceInfo
import com.satguru.veritask.models.Users
import com.satguru.veritask.ui.features.destinations.SalesDestination
import com.satguru.veritask.ui.features.destinations.UsersScreenDestination
import com.satguru.veritask.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel(
    private val application: Application,
    protected val repositoryService: RepositoryService
) : ViewModel() {

    private val _uiStateForLoginApi = MutableStateFlow<UiState<DeviceInfo>>(UiState.Ideal)
    val uiStateForLoginData get() = _uiStateForLoginApi.asStateFlow()

    private val _selectedUser = MutableStateFlow<Users?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    fun setSelectedUser(users: Users?) {
        _selectedUser.value = users
    }

    fun checkLoginState(userLoggedIn: () -> Unit, userNotLoggedIn: () -> Unit) {
        if (repositoryService.getSharedPreference().isLoggedIn()) {
            userLoggedIn()
        } else {
            userNotLoggedIn()
        }
    }

    fun login(
        loggedInUser: Users,
    ) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                _uiStateForLoginApi.value = UiState.Error(task.exception ?: Exception("SWW"))
                return@OnCompleteListener
            }
            val token = task.result
            val deviceInfo =
                Constants.createDeviceTokenRequestObject(application, token, loggedInUser.id)
            repositoryService.login(deviceInfo).onEach {
                when (it) {
                    is UiState.Loading -> {
                        _uiStateForLoginApi.value = UiState.Loading
                    }

                    is UiState.Error -> {
                        _uiStateForLoginApi.value = UiState.Error(it.error)
                    }

                    is UiState.Success -> {
                        repositoryService.getSharedPreference().setLoggedInUser(loggedInUser)
                        _uiStateForLoginApi.value = UiState.Success(it.data.data)
                    }
                }
            }.launchIn(viewModelScope)
        })
    }

    fun navigateToNextPostLogin(
        navigator: DestinationsNavigator
    ) {
        navigator.navigate(route = SalesDestination.route) {
            popUpTo(route = UsersScreenDestination.route) {
                inclusive = true
            }
        }
    }

    fun logout(navigator: DestinationsNavigator, onLogout: (Users) -> Unit = {}) {
        val preference = repositoryService.getSharedPreference()
        if (preference.isLoggedIn()) {
            val user = preference.requireLoggedInUser()
            preference.setLoggedInUser(null)
            navigator.navigate(route = UsersScreenDestination.route) {
                popUpTo(route = SalesDestination.route) {
                    inclusive = true
                }
            }
            onLogout(user)
        }
    }

    fun getLoggedInUser() = repositoryService.getSharedPreference().getLoggedInUser()
}