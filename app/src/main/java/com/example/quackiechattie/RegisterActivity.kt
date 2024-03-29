package com.example.quackiechattie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = RegisterActivity::class.java.simpleName
    lateinit var mSock: Socket;
    lateinit var uName: String;
    lateinit var uid: String;

    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        submitButton.setOnClickListener(this)

        mSock = SocketHandler.getSocket()
    }

    private fun signUp() {
        // connections
        uName = regEnterUname.text.toString()
        uid = User.getUserID()

        if(!uName.isNullOrBlank()) {
            val data = initData(uName, uid)
            val jData = gson.toJson(data)

            mSock.emit("checkUsername", jData)
            mSock.on("NEW_USER", addUserName)
            mSock.on("USER_EXISTS", warnUserExists)
        } else {
            Toast.makeText(this, "Please Enter a Username", Toast.LENGTH_SHORT).show()
        }
    }

    private var addUserName = Emitter.Listener {
        Log.d("addUserName", "Entering function")
        val data = initData(uName, uid)
        val jData = gson.toJson(data)
        User.setUser(uName)
        val intent = Intent(this, ChatRoomsActivity::class.java)
        mSock.emit("signUp", jData)
        closeListeners()
        startActivity(intent)
    }

    private var warnUserExists = Emitter.Listener {
        Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.submitButton -> signUp()
        }
    }

    private fun closeListeners() {
        mSock.off("NEW_USER")
        mSock.off("USER_EXISTS")
    }
}