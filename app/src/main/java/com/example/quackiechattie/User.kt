package com.example.quackiechattie

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

object User {
    private lateinit var uName: String
    private lateinit var userID: String
    private lateinit var user: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private var isLoggedIn: Boolean = false
    private var firstTime: Boolean = false



    @Synchronized
    fun setFireBaseUser(u: FirebaseUser?): Boolean {
        if (u != null) {
            user = u
            return setUserID(user)
        }
    }

    @Synchronized
    fun getFireBaseUser(): FirebaseUser {
        return user
    }


    fun logout() {
        isLoggedIn = false
    }

    @Synchronized
    fun setUser(userName: String) {
        uName = userName
    }

    @Synchronized
    fun getUsername(): String {
        return uName
    }

    @Synchronized
    fun setUserID(user: FirebaseUser?): Boolean {
        val id = user?.uid
        return if (id != null) {
            Log.d("USER", id)
            userID = id
            true
        } else {
            false
        }
    }

    @Synchronized
    fun getUserID(): String {
        return userID
    }
}