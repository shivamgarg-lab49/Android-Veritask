package com.satguru.veritask.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.satguru.veritask.R
import com.satguru.veritask.models.Sales
import com.satguru.veritask.models.User

object PreviewDataProvider {
    fun getUsers(context: Context): List<User> {
        val typeToken = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson<List<User>>(
            context.resources.openRawResource(R.raw.users).bufferedReader(), typeToken
        ).orEmpty()
    }

    fun getDeals(context: Context): List<Sales> {
        val typeToken = object : TypeToken<List<Sales>>() {}.type
        return Gson().fromJson<List<Sales>>(
            context.resources.openRawResource(R.raw.deals).bufferedReader(), typeToken
        ).orEmpty()
    }
}