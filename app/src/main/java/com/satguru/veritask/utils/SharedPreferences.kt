package com.satguru.veritask.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.satguru.veritask.models.Users

class SharedPreferences(application: Application) {
    companion object {
        private const val PREFS_NAME = "vt-preferences"
        private const val SAVED_USER = "v_saved_user_info"
        private const val MY_DEALS = "v_my_deal_on"
    }

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setLoggedInUser(user: Users?) {
        editor.putString(SAVED_USER, Gson().toJson(user))
        editor.apply()
    }

    fun getLoggedInUser(): Users? {
        val savedUsers = sharedPreferences.getString(SAVED_USER, "") ?: ""
        try {
            val user = Gson().fromJson(savedUsers, Users::class.java)
            if (user.id.isNotEmpty()) {
                return user
            }
        } catch (_: Exception) {
        }
        return null
    }

    fun requireLoggedInUser(): Users {
        val loggedInUser = getLoggedInUser()
        if (loggedInUser != null) {
            return loggedInUser
        }
        throw Exception("No user info exist")
    }

    fun isLoggedIn(): Boolean {
        return try {
            requireLoggedInUser()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun setMyDealOn(onOrOff: Boolean) {
        editor.putBoolean(MY_DEALS, onOrOff)
        editor.apply()
    }

    fun isMyDealOn(): Boolean {
       return sharedPreferences.getBoolean(MY_DEALS, false)
    }
}
