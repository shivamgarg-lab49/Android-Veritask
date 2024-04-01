package com.satguru.veritask.ui.features.reject.vm

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.satguru.veritask.di.RepositoryService
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.BaseViewModel
import com.satguru.veritask.models.RejectReasonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RejectPopupViewModel @Inject constructor(
    application: Application,
    repositoryService: RepositoryService,
) : BaseViewModel(application, repositoryService) {

    private var _reasonsJob: Job? = null
    private val _uiStateForReasonsData =
        MutableStateFlow<UiState<List<RejectReasonItem>>>(UiState.Ideal)

    val uiStateForReasonsData get() = _uiStateForReasonsData.asStateFlow()

    init {
        fetchReasons()
    }

    fun fetchReasons() {
        if (_uiStateForReasonsData.value is UiState.Success<List<RejectReasonItem>>) {
            _uiStateForReasonsData.value =
                (_uiStateForReasonsData.value as UiState.Success<List<RejectReasonItem>>)
            return
        }
        _reasonsJob?.cancel()
        _reasonsJob = repositoryService.getRejectReasons()
            .onEach {
                when (it) {
                    is UiState.Loading -> {
                        _uiStateForReasonsData.value = UiState.Loading
                    }

                    is UiState.Error -> {
                        _uiStateForReasonsData.value = UiState.Error(it.error)
                    }

                    is UiState.Success -> {
                        _uiStateForReasonsData.value = UiState.Success(it.data.data)
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}