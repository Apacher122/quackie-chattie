package com.example.quackiechattie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.Exception

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = RegisterActivity::class.java.simpleName
    lateinit var mSock: Socket;
    lateinit var uName: String;
    lateinit var pWord: String;

    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        submitButton.setOnClickListener(this)

        mSock = SocketHandler.getSocket()
    }

    private fun signUp() {
        // connections
        val uName = regEnterUname.text.toString()
        val password = regEnterPassword.text.toString()

        if(!password.isNullOrBlank()&&!uName.isNullOrBlank()) {
            val intent = Intent(this, LoginActivity::class.java)
            val data = initData(uName, password)
            val jData = gson.toJson(data)

            mSock.emit("signUp", jData)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Username and/or password cannot be empty.", Toast.LENGTH_SHORT)
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.submitButton -> signUp()
        }
    }
}