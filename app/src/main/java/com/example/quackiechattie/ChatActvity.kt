package com.example.quackiechattie

import android.app.Notification
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_room.*
import com.example.quackiechattie.model.Notification as Notifs

class ChatActvity : AppCompatActivity(), View.OnClickListener {
    val TAG = ChatActvity::class.java.simpleName

    lateinit var mSock: Socket;
    lateinit var uName: String;
    lateinit var rName: String;

    val gson: Gson = Gson()

    val chats: ArrayList<Chat> = arrayListOf()
    lateinit var chatActivityAdapter : ChatActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        send.setOnClickListener(this)
        leave.setOnClickListener(this)

        try {
            uName = intent.getStringExtra("uName")!!
            rName = intent.getStringExtra("rName")!!
        } catch (e: Exception) {
            e.printStackTrace()
        }

        chatActivityAdapter = ChatActivityAdapter(this, chats)
        recyclerView.adapter = chatActivityAdapter

        mSock.on("userJoined", onUser)
        mSock.on("userLeft", onLeave)
        mSock.on("newMessage", onNewMessage)
    }

    var onUser = Emitter.Listener {
        val user = it[0] as String
        val chat = Chat(user, "", rName, Notifs.JOIN.index, false)
        addToRecyclerView(chat)
    }

    var onLeave = Emitter.Listener {
        val user = it[0] as String
        val chat = Chat(user, "", "", Notifs.LEFT.index, false)
        addToRecyclerView(chat)
    }

    var onNewMessage = Emitter.Listener {
        val chat: Chat = gson.fromJson(it[0].toString(), Chat::class.java)
        chat.viewType = Notifs.RECV.index
        addToRecyclerView(chat)
    }


    private fun addToRecyclerView(chat: Chat) {
        runOnUiThread {
            chats.add(chat)
            chatActivityAdapter.notifyItemInserted(chats.size)
            editText.setText("")
            recyclerView.scrollToPosition(chats.size - 1)
        }
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.send -> sendMessage()
            R.id.leave -> onDestroy()
        }
    }

    private fun sendMessage() {
        val msg = editText.text.toString()
        if (msg.isNotEmpty()) {
            val send = Send(uName, msg, rName)
            val jData = gson.toJson(send)
            mSock.emit("newMessage", jData)
            val chat = Chat(uName, msg, rName, Notifs.SENT.index, true)
            addToRecyclerView(chat)
        }
    }
}