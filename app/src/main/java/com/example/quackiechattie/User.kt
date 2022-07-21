package com.example.quackiechattie

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class User @Inject constructor(private val encryptedSharedPreferences: SharedPreferences) {
    private lateinit var username: String
    private lateinit var userID: String
    private lateinit var user: FirebaseUser
    private var isLoggedIn: Boolean = false
    private var firstTime: Boolean = false

    @Synchronized
    fun getFirstTimeStatus(): Boolean {
        return firstTime
    }

    @Synchronized
    fun setFireBaseUser(u: FirebaseUser) {
        user = u
    }

    @Synchronized
    fun getFireBaseUser(): FirebaseUser {
        return user
    }

    @Synchronized
    fun login() {
        isLoggedIn = true
        firstTime = false
    }

    @Synchronized
    fun checkLoginStatus(): Boolean {
        return isLoggedIn
    }

    fun logout() {
        isLoggedIn = false
    }

    @Synchronized
    fun setUser(userName: String) {
        username = userName
        kotlin.runCatching {
            encryptedSharedPreferences.edit().putString("username", userName)
        }
    }

    @Synchronized
    fun getUsername(): String {
        username = encryptedSharedPreferences.getString("username", "").toString()
        return username
    }

    @Synchronized
    fun setUserID(id: String) {
        userID = id
    }

    @Synchronized
    fun getUserID(): String {
        return userID
    }
}