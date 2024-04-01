package com.satguru.veritask.utils

object DeepLinkBuilder {

    const val DEALS_DETAIL_URI_PATTERN = "veritask://veritask/deals/{dealId}/{action}"

    fun createDealsDetailDeepLink(dealId: String, action: String): String {
        return DEALS_DETAIL_URI_PATTERN.replace("{dealId}", dealId).replace("{action}", action)
    }
}