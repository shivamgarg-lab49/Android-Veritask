package com.satguru.veritask.models

import com.google.gson.annotations.SerializedName

data class DealApproveRequest(
    @SerializedName("approverId")
    val approverId: String,
    @SerializedName("status")
    val status: String
)

data class DealRejectRequest(
    @SerializedName("approverId")
    val approverId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("reason")
    val reason: String?
)