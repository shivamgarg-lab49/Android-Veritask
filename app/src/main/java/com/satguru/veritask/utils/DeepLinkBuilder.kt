package com.satguru.veritask.utils

object DeepLinkBuilder {

    const val DEALS_DETAIL_URI_PATTERN = "veritask://veritask/deals/{dealId}"

    fun createDealsDetailDeepLink(dealId: String): String {
        return DEALS_DETAIL_URI_PATTERN.replace("{dealId}",dealId)
    }
}