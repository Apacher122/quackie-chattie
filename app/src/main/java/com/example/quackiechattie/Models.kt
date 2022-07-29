/*
    data classes for parsing JSON data from server
 */
package com.example.quackiechattie

data class Chat(val uName: String, val content: String, val rName: String, var viewType: Int, var newMessage: Boolean)
data class Rooms(val room_id: String, val room_name: String, val user_name: String)
data class initData(val uName: String, val uid: String)
data class Send(val uName: String, val content: String, val rName: String)
data class Messages(val sender: String, val message_text: String, val room_name: String)