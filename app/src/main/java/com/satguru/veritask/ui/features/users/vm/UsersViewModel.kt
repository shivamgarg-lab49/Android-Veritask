package com.satguru.veritask.ui.features.users.vm

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.satguru.veritask.BaseViewModel
import com.satguru.veritask.di.RepositoryService
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    application: Application,
    repositoryService: RepositoryService,
) : BaseViewModel(application, repositoryService) {

    private var _usersJob: Job? = null
    private val _uiStateForUserData = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val uiStateForUsersData = _uiStateForUserData.asStateFlow()

    fun fetch() {
        _usersJob?.cancel()
        _usersJob = repositoryService.getUsers().onEach { it ->
            when (it) {
                is UiState.Loading -> {
                    _uiStateForUserData.value = UiState.Loading
                }

                is UiState.Error -> {
                    _uiStateForUserData.value = UiState.Error(it.error)
                }

                is UiState.Success -> {
                    _uiStateForUserData.value =
                        UiState.Success(it.data.data.filter { user -> user.role.lowercase() == "Manager".lowercase() })
                }
            }
        }.launchIn(viewModelScope)
    }
}