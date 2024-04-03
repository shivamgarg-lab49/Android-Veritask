package com.satguru.veritask.di

import com.satguru.veritask.extensions.UiState
import com.satguru.veritask.models.BaseResponse
import com.satguru.veritask.models.DealApproveRequest
import com.satguru.veritask.models.DealRejectRequest
import com.satguru.veritask.models.DeviceInfo
import com.satguru.veritask.models.RejectReasonItem
import com.satguru.veritask.models.Sales
import com.satguru.veritask.models.User
import com.satguru.veritask.utils.SharedPreferences
import kotlinx.coroutines.flow.Flow

interface RepositoryService {

    fun getUsers(): Flow<UiState<BaseResponse<List<User>>>>
    fun getDeals(): Flow<UiState<BaseResponse<List<Sales>>>>
    fun getDealDetails(dealId: String): Flow<UiState<BaseResponse<Sales>>>

    fun getRejectReasons(): Flow<UiState<BaseResponse<List<RejectReasonItem>>>>

    fun approveDeal(
        dealId: String, requestBody: DealApproveRequest
    ): Flow<UiState<BaseResponse<Sales>>>

    fun rejectDeal(
        dealId: String, requestBody: DealRejectRequest
    ): Flow<UiState<BaseResponse<Sales>>>

    fun login(requestBody: DeviceInfo): Flow<UiState<BaseResponse<DeviceInfo>>>

    fun getSharedPreference(): SharedPreferences
}