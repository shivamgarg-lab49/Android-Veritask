package com.satguru.veritask.ui.features.sales.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.satguru.veritask.BaseViewModel
import com.satguru.veritask.di.RepositoryService
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.models.Sales
import com.satguru.veritask.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    application: Application,
    repositoryService: RepositoryService,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel(application, repositoryService) {

    private var _dealsJob: Job? = null
    private val _indexMapping =
        linkedMapOf(0 to Constants.PENDING, 1 to Constants.APPROVED, 2 to Constants.REJECTED)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    private val _isMyDealOn = MutableStateFlow(repositoryService.getSharedPreference().isMyDealOn())
    val isMyDealOn get() = _isMyDealOn.asStateFlow()

    val selectedTabIndex = savedStateHandle.getStateFlow(
        SALES_SELECTED_TAB_INDEX_KEY, 0
    )

    enum class OpType {
        Pull,
        Fresh,
    }

    private val _uiStateForSalesData = MutableStateFlow<UiState<List<Sales>>>(UiState.Ideal)
    val uiStateForSalesData =
        combine(selectedTabIndex, _uiStateForSalesData, _isMyDealOn) { t1, t2, t3 ->
            when (t2) {
                UiState.Ideal -> t2
                UiState.Loading -> t2
                is UiState.Error -> t2
                is UiState.Success -> {
                    val loggedInUserId = getLoggedInUser()?.id
                    UiState.Success(
                        t2.data
                            .filter { sales -> sales.status == _indexMapping[t1] }
                            .filter { sales -> return@filter if (loggedInUserId.isNullOrBlank() || t3.not()) true else loggedInUserId == sales.approverId }
                    )
                }

                else -> {
                    throw IllegalStateException("Unknown state, development issue")
                }
            }
        }

    fun toggleMyDeal() {
        _isMyDealOn.value = _isMyDealOn.value.not()
        repositoryService.getSharedPreference().setMyDealOn(_isMyDealOn.value)
    }

    fun changeSelectedTabIndex(selectedTabIndex: Int) {
        require(
            value = _indexMapping.contains(selectedTabIndex)
        ) { "Invalid index passed" }
        savedStateHandle[SALES_SELECTED_TAB_INDEX_KEY] = selectedTabIndex
    }

    fun fetch(opType: OpType) {
        _dealsJob?.cancel()
        _dealsJob = repositoryService.getDeals().onEach {
            when (it) {
                is UiState.Loading -> {
                    if (opType == OpType.Pull) {
                        _isRefreshing.value = true
                    } else if (opType == OpType.Fresh) {
                        _uiStateForSalesData.value = UiState.Loading
                    }
                }

                is UiState.Error -> {
                    if (opType == OpType.Pull) {
                        _isRefreshing.value = false
                    } else if (opType == OpType.Fresh) {
                        _uiStateForSalesData.value = UiState.Error(it.error)
                    }
                }

                is UiState.Success -> {
                    _uiStateForSalesData.value = UiState.Success(it.data.data)
                    if (opType == OpType.Pull) {
                        _isRefreshing.value = false
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    companion object {
        private const val SALES_SELECTED_TAB_INDEX_KEY = "selected_tab_index"
    }
}