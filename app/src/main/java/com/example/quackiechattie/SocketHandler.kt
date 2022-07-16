package com.example.quackiechattie

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    lateinit var mSock: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSock = IO.socket("https://floating-headland-71614.herokuapp.com")
            Log.d("success", "Connected to server")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSock
    }

    @Synchronized
    fun establishConnection() {
        mSock.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSock.disconnect()
    }
}
