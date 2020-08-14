package com.example.bestoffer.utils

import android.content.Context
import com.example.bestoffer.models.UiUser
import com.google.gson.Gson


fun clearLoginSession(context: Context) {
    val editor = context.getSharedPreferences(AppSharedPrefs, 0).edit()
    editor.putBoolean(isUserLoggedInKey, false)
    editor.apply()
}

fun getLoggedInUser(context: Context): UiUser {
    val sharedPreferences = context.getSharedPreferences(AppSharedPrefs, 0)
    val string = sharedPreferences.getString(currentUserKey, "")
    return Gson().fromJson(string, UiUser::class.java)
}

fun getLoginSession(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(AppSharedPrefs, 0)
    return sharedPreferences.getBoolean(isUserLoggedInKey, false)
}

fun saveLoginSession(context: Context, UiUser: UiUser) {
    val sharedPreferences = context.getSharedPreferences(AppSharedPrefs, 0)
    val editor = sharedPreferences.edit()
    val string2 = Gson().toJson(UiUser as Any?)
    editor.putBoolean(isUserLoggedInKey, true)
    editor.putString(currentUserKey, string2)
    editor.apply()
}

const val AppSharedPrefs: String = "AppSharedPrefs"
const val isUserLoggedInKey: String = "isUserLoggedInKey"
const val currentUserKey: String = "currentUserKey"


