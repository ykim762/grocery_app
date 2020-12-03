package com.yeji.day13_excercise.helper

import android.content.Context
import com.yeji.day13_excercise.models.Order
import com.yeji.day13_excercise.models.User

class SessionManager(var mContext: Context) {
    private val FILE_NAME = "my_pref"
    private val KEY_ID = "id"
    private val KEY_NAME = "name"
    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"

    var sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()

    fun register(user: User) {
        editor.putString(KEY_NAME, user.name)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PASSWORD, user.password)

        editor.commit()
    }

    fun login(id:String, name:String, email:String): Boolean {
        editor.putString(KEY_ID, id)
        editor.putString(KEY_NAME, name)
        editor.putString(KEY_EMAIL, email)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()

        return true
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUser(): User {
        var id = sharedPreferences.getString(KEY_ID, null)
        var name = sharedPreferences.getString(KEY_NAME, null)
        var email = sharedPreferences.getString(KEY_EMAIL, null)
        var password = sharedPreferences.getString(KEY_PASSWORD, null)

        var user = User(id, name, email, password)
        return user
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }

}