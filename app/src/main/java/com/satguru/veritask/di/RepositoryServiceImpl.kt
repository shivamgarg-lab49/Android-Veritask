package com.satguru.veritask.di

import com.satguru.veritask.extensions.networkCall
import com.satguru.veritask.models.BaseResponse
import com.satguru.veritask.models.DealApproveRequest
import com.satguru.veritask.models.DealRejectRequest
import com.satguru.veritask.models.DeviceInfo
import com.satguru.veritask.models.Sales
import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.models.RejectReasonItem
import com.satguru.veritask.models.User
import com.satguru.veritask.services.ApiService
import com.satguru.veritask.utils.SharedPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryServiceImpl @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : RepositoryService {
    override fun getUsers(): Flow<UiState<BaseResponse<List<User>>>> {
        return networkCall { apiService.getUsers() }
    }

    override fun getDeals(): Flow<UiState<BaseResponse<List<Sales>>>> {
        return networkCall { apiService.getDeals() }
    }

    override fun getDealDetails(dealId: String): Flow<UiState<BaseResponse<Sales>>> {
        return networkCall { apiService.getDealDetails(dealId) }
    }

    override fun getRejectReasons(): Flow<UiState<BaseResponse<List<RejectReasonItem>>>> {
        return networkCall { apiService.rejectReasons() }
    }

    override fun approveDeal(
        dealId: String, requestBody: DealApproveRequest
    ): Flow<UiState<BaseResponse<Sales>>> {
        return networkCall { apiService.approveDeal(dealId, requestBody) }
    }

    override fun rejectDeal(
        dealId: String, requestBody: DealRejectRequest
    ): Flow<UiState<BaseResponse<Sales>>> {
        return networkCall { apiService.rejectDeal(dealId, requestBody) }
    }

    override fun login(requestBody: DeviceInfo): Flow<UiState<BaseResponse<DeviceInfo>>> {
        return networkCall { apiService.login(requestBody) }
    }


    override fun getSharedPreference(): SharedPreferences {
        return sharedPreferences;
    }
}