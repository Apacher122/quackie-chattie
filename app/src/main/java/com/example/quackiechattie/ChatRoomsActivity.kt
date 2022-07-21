/*
    Communicate with the Socket.io server
 */

package com.example.quackiechattie

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_room.*
import com.example.quackiechattie.model.Notification
import kotlinx.android.synthetic.main.activity_menu.*
import kotlin.Exception


class ChatRoomsActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = ChatRoomsActivity::class.java.simpleName

    lateinit var mSock: Socket;
    lateinit var uName: String;


    val gson: Gson = Gson()

    // RecyclerView stuffs
    val rooms: ArrayList<Rooms> = arrayListOf();
    lateinit var quackieAdapter: QuackieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        send.setOnClickListener(this)
        leave.setOnClickListener(this)

        mSock = SocketHandler.getSocket();

        try {
            uName = intent.getStringExtra("uName")!!
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        mSock.on(Socket.EVENT_CONNECT, onConnect)
//        mSock.on("userJoined", onUser)
//        mSock.on("update", onUpdate)
//        mSock.on("userLeft", onLeave)
    }


//    var onLeave = Emitter.Listener {
//        val user = it[0] as String
//        val chat: Chat = Chat(user, "", "", Notification.LEFT.index)
//        addToRecyclerView(chat)
//    }
//
//    var onUpdate = Emitter.Listener {
//        val chat: Chat = gson.fromJson(it[0].toString(), Chat::class.java)
//        chat.viewType = Notification.RECV.index
//        addToRecyclerView(chat)
//    }
//
//    var onConnect = Emitter.Listener {
//        val data = initData(uName, password)
//        val jData = gson.toJson(data)
//        mSock.emit("subscribe", jData)
//    }
//
//
//    var onUser = Emitter.Listener {
//        val user = it[0] as String
//        val chat = Chat(user, "", rName, Notification.JOIN.index)
//        addToRecyclerView(chat)
//        Log.d(TAG, "onUser Triggered")
//    }
//
//    private fun addToRecyclerView(chat: Chat) {
//        runOnUiThread {
//            chats.add(chat)
//            quackieAdapter.notifyItemInserted(chats.size)
//            editText.setText("")
//            recyclerView.scrollToPosition(chats.size - 1)
//        }
//    }
//
//    private fun sendMessage() {
//        val msg = editText.text.toString()
//        if (msg.isNotEmpty()) {
//            var mediaPlayer = MediaPlayer.create(this, R.raw.quack)
//            mediaPlayer.start()
//            val send = Send(uName, msg, rName)
//            val jData = gson.toJson(send)
//            mSock.emit("newMessage", jData)
//
//            val chat = Chat(uName, msg, rName, Notification.SENT.index)
//            addToRecyclerView(chat)
//        }
//    }
//
    override fun onClick(p0: View?) {
        when (p0!!.id) {
//            R.id.send -> sendMessage()
            R.id.leave -> onDestroy()
        }
    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        val data = initData(uName, password)
//        val jData = gson.toJson(data)
//        mSock.emit("unsubscribe", jData)
//        mSock.disconnect()
//    }
}