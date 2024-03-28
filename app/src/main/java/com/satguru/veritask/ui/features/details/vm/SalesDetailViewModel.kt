package com.satguru.veritask.ui.features.details.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.satguru.veritask.di.RepositoryService
import com.satguru.veritask.models.DealApproveRequest
import com.satguru.veritask.models.DealRejectRequest
import com.satguru.veritask.models.Sales
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.utils.Constants
import com.satguru.veritask.BaseViewModel
import com.satguru.veritask.R
import com.satguru.veritask.ui.features.destinations.SalesDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SalesDetailViewModel @Inject constructor(
    application: Application,
    repositoryService: RepositoryService,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel(application, repositoryService) {

    private var _dealsJob: Job? = null
    private val _uiStateForSalesData = MutableStateFlow<UiState<Sales>>(UiState.Ideal)

    val uiStateForSalesDetailData get() = _uiStateForSalesData.asStateFlow()

    private val _uiStateSalesRejectCall = MutableStateFlow<UiState<Int>>(UiState.Ideal)
    val uiStateSalesRejectCall get() = _uiStateSalesRejectCall.asStateFlow()

    init {
        fetch(getDealId())
    }

    fun fetch(dealId: String) {
        _dealsJob?.cancel()
        _dealsJob = repositoryService.getDealDetails(dealId)
            .onEach {
                when (it) {
                    is UiState.Loading -> {
                        _uiStateForSalesData.value = UiState.Loading
                    }

                    is UiState.Error -> {
                        _uiStateForSalesData.value = UiState.Error(it.error)
                    }

                    is UiState.Success -> {
                        _uiStateForSalesData.value = UiState.Success(it.data.data)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun approve() {
        val dealId = getDealId()
        val body = DealApproveRequest(
            approverId = getCurrentLoggedInUserId(),
            status = Constants.APPROVED
        )
        _dealsJob?.cancel()
        _dealsJob = repositoryService.approveDeal(dealId, body)
            .onEach {
                when (it) {
                    is UiState.Loading -> {
                        _uiStateSalesRejectCall.value = UiState.Loading
                    }

                    is UiState.Error -> {
                        _uiStateSalesRejectCall.value = UiState.Error(it.error)
                    }

                    is UiState.Success -> {
                        _uiStateForSalesData.value = UiState.Success(it.data.data)
                        _uiStateSalesRejectCall.value =
                            UiState.Success(R.string.successfully_approved)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun reject(reason: String? = "Please make the changes ASAP") {
        val dealId = getDealId()
        val body = DealRejectRequest(
            approverId = getCurrentLoggedInUserId(),
            status = Constants.REJECTED,
            reason = reason
        )
        _dealsJob = repositoryService.rejectDeal(dealId, body)
            .onEach {
                when (it) {
                    is UiState.Loading -> {
                        _uiStateSalesRejectCall.value = UiState.Loading
                    }

                    is UiState.Error -> {
                        _uiStateSalesRejectCall.value = UiState.Error(it.error)
                    }

                    is UiState.Success -> {
                        _uiStateForSalesData.value = UiState.Success(it.data.data)
                        _uiStateSalesRejectCall.value =
                            UiState.Success(R.string.successfully_rejected)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getDealId(): String {
        return SalesDetailsDestination.argsFrom(savedStateHandle).dealId
    }

    private fun getCurrentLoggedInUserId(): String {
        if (repositoryService.getSharedPreference().isLoggedIn()) {
            return repositoryService.getSharedPreference().requireLoggedInUser().id
        }
        throw Exception("User not Logged In")
    }

    fun isSameManagerLoggedIn(managerId: String): Boolean {
        return repositoryService.getSharedPreference().getLoggedInUser()?.id.orEmpty() == managerId && managerId.isNotEmpty()
    }
}