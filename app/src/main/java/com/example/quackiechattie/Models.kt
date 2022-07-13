/*
    data classes for parsing JSON data from server
 */
package com.example.quackiechattie

data class Chat(val uName: String, val content: String, val rName: String, var viewType: Int)
data class initData(val uName: String, val rName: String)
data class Send(val uName: String, val content: String, val rName: String)