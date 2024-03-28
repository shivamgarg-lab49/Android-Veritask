package com.satguru.veritask.services

import com.satguru.veritask.models.BaseResponse
import com.satguru.veritask.models.DealApproveRequest
import com.satguru.veritask.models.DealRejectRequest
import com.satguru.veritask.models.DeviceInfo
import com.satguru.veritask.models.Sales
import com.satguru.veritask.models.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/users")
    suspend fun getUsers(): Response<BaseResponse<List<Users>>>
    @GET("api/deals")
    suspend fun getDeals(): Response<BaseResponse<List<Sales>>>

    @GET("api/deals/{dealId}")
    suspend fun getDealDetails(@Path("dealId") dealId: String): Response<BaseResponse<Sales>>

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
}