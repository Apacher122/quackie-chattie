package com.example.quackiechattie

object User {
    private lateinit var username: String
    private lateinit var userID: String

    @Synchronized
    fun setUser(userName: String) {
        username = userName
    }

    @Synchronized
    fun getUsername(): String {
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