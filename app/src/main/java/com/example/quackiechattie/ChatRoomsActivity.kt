/*
    Communicate with the Socket.io server
 */

package com.example.quackiechattie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_roomslist.*
import kotlin.Exception


class ChatRoomsActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = ChatRoomsActivity::class.java.simpleName

    lateinit var mSock: Socket;
    lateinit var uName: String;

    val gson: Gson = Gson()

    // RecyclerView stuffs
    val rooms: ArrayList<Rooms> = arrayListOf();
    lateinit var chatRoomsActivityAdapter: ChatRoomsActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        uName = User.getUsername()
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roomslist)

        join.setOnClickListener(this)
        logoutButton.setOnClickListener(this)

        mSock = SocketHandler.getSocket();
    }

    override fun onClick(p0: View?) {
       when(p0?.id) {
           R.id.join -> joinRoom()
           R.id.logoutButton -> logout()
       }
    }

    private fun joinRoom() {
        val uName = User.getUsername()
        val rName = rName.text.toString()

        if(!rName.isNullOrBlank()&&!uName.isNullOrBlank()) {
            val intent = Intent(this, ChatActvity::class.java)
            intent.putExtra("uName", uName)
            intent.putExtra("rName", rName)
            val data = Rooms(rName, uName)
            val jData = gson.toJson(data)
            mSock.emit("CREATE_OR_JOIN", jData)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please enter or join a room", Toast.LENGTH_SHORT).show()
        }
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

//
//    override fun onDestroy() {
//        super.onDestroy()
//        val data = initData(uName, password)
//        val jData = gson.toJson(data)
//        mSock.emit("unsubscribe", jData)
//        mSock.disconnect()
//    }
}