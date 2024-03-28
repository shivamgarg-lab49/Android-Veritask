package com.satguru.veritask.models

import com.google.gson.annotations.SerializedName


import androidx.annotation.Keep

@Keep
data class BaseResponse<T>(
    val `data`: T,
    val message: String,
    val status: String
)


@Keep
data class Sales(
    val id: String,
    val approver: Approver,
    val approverId: String,
    val client: Client,
    val clientId: String,
    val createdAt: String,
    val creator: Creator,
    val creatorId: String,
    val reason: String?,
    val status: String,
    val totalQuantity: Int,
    val transactionValue: String,
    val updatedAt: String,
    val details: List<Detail>? = null,
)

@Keep
data class Approver(
    val email: String,
    val name: String
)

@Keep
data class Client(
    val name: String
)

@Keep
data class Creator(
    val email: String,
    val name: String
)

@Keep
data class Detail(
    val dealId: String,
    val id: String,
    val productId: String,
    val productName: String,
    val productQuantity: Int,
    val productTotalPrice: String,
    val productUnitPrice: String
)

@Keep
data class DeviceInfo(
    val userId: String,
    val os: String,
    val token: String,
    val deviceId: String,
)

@Keep
data class Users(
    val id: String,
    val name: String,
    val role: String,
    val email: String,
    val manager: Manager?,
)

@Keep
data class Manager(
    val id: String,
    val name: String,
    val email: String,
)