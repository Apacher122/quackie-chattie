/*
    data classes for parsing JSON data from server
 */
package com.example.quackiechattie

data class Chat(val uName: String, val content: String, val rName: String, var viewType: Int, var newMessage: Boolean)
data class Rooms(val rName: String, val uName: String)
data class initData(val uName: String, val uid: String)
data class Send(val uName: String, val content: String, val rName: String)