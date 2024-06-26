package com.satguru.veritask.services

import com.satguru.veritask.models.BaseResponse
import com.satguru.veritask.models.DealApproveRequest
import com.satguru.veritask.models.DealRejectRequest
import com.satguru.veritask.models.DeviceInfo
import com.satguru.veritask.models.LogoutRequestResponse
import com.satguru.veritask.models.RejectReasonItem
import com.satguru.veritask.models.Sales
import com.satguru.veritask.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/users")
    suspend fun getUsers(): Response<BaseResponse<List<User>>>

    @GET("api/deals")
    suspend fun getDeals(@Header("userId") managerId: String): Response<BaseResponse<List<Sales>>>

    @GET("api/deals/{dealId}")
    suspend fun getDealDetails(@Path("dealId") dealId: String): Response<BaseResponse<Sales>>

    @GET("api/reject-reasons")
    suspend fun rejectReasons(): Response<BaseResponse<List<RejectReasonItem>>>

    @PATCH("api/deals/{dealId}")
    suspend fun approveDeal(
        @Path("dealId") dealId: String,
        @Body requestBody: DealApproveRequest
    ): Response<BaseResponse<Sales>>

    @PATCH("api/deals/{dealId}")
    suspend fun rejectDeal(
        @Path("dealId") dealId: String,
        @Body requestBody: DealRejectRequest
    ): Response<BaseResponse<Sales>>

    @POST("api/login")
    suspend fun login(@Body requestBody: DeviceInfo): Response<BaseResponse<DeviceInfo>>

    @POST("api/logout")
    suspend fun logout(@Body requestBody: LogoutRequestResponse): Response<BaseResponse<Int>>
}