package com.example.quackiechattie

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest


object User {
    private lateinit var uName: String
    private lateinit var userID: String
    private lateinit var user: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private var isLoggedIn: Boolean = false
    private var firstTime: Boolean = false



    @Synchronized
    fun setFireBaseUser(u: FirebaseUser?): Boolean {
        var status = false
        if (u != null) {
            user = u
             status = setUserID(user)
        }
        return status
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
        val profileUpdates = userProfileChangeRequest {
            displayName = userName
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SetUser", "User profile updated.")
                } else {
                    Log.d("SetUser", "Failed to update profile.")
                }
            }
    }



    @Synchronized
    fun getUsername(): String {
        uName = user.displayName.toString()
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