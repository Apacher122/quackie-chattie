/*
    For usernames and rooms to enter
 */

package com.example.quackiechattie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter


import kotlinx.android.synthetic.main.activity_menu.*



class LoginActivity : AppCompatActivity(), View.OnClickListener {
    val gson: Gson = Gson()
    var isLoggedin = false
    lateinit var mSock: Socket;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSock = SocketHandler.getSocket()

        loginButton.setOnClickListener(this)
        regButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.loginButton -> login()
            R.id.regButton -> register()
        }
    }

    private fun login() {
        val uName = uName.text.toString()
        val password = passwordInput.text.toString()

        val data = initData(uName, password)
        val jData = gson.toJson(data)

        if(!password.isNullOrBlank()&&!uName.isNullOrBlank()) {
            mSock.emit("login", jData)
            mSock.on("LOGIN_FAIL", restartLogin)
            mSock.on("login", goToRooms)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please enter valid username and password", Toast.LENGTH_SHORT)
        }
    }

    var restartLogin = Emitter.Listener {
        SocketHandler.closeConnection()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    var goToRooms = Emitter.Listener {
        val intent = Intent(this, QuackieActivity::class.java)
        startActivity(intent)
    }

    private fun register() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}