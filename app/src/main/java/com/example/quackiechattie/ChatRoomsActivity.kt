/*
    Communicate with the Socket.io server
 */

package com.example.quackiechattie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_roomslist.*


class ChatRoomsActivity : AppCompatActivity(), View.OnClickListener, ChatRoomsActivityAdapter.OnItemClickListener {
    val TAG = ChatRoomsActivity::class.java.simpleName

    lateinit var mSock: Socket;
    lateinit var uName: String;
    lateinit var uid: String;

    val gson: Gson = Gson()

    // RecyclerView stuffs
    val rooms: ArrayList<Rooms> = arrayListOf();
    lateinit var chatRoomsActivityAdapter: ChatRoomsActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roomslist)

        uName = User.getUsername()
        uid = User.getUserID()
        val data = initData(uName, uid)
        val jData = gson.toJson(data)

        chatRoomsActivityAdapter = ChatRoomsActivityAdapter(this, rooms, this)
        roomList.adapter = chatRoomsActivityAdapter

        val layoutManager = LinearLayoutManager(this)
        roomList.layoutManager = layoutManager


        join.setOnClickListener(this)
        logoutButton.setOnClickListener(this)

        mSock = SocketHandler.getSocket();
        mSock.emit("getRooms", jData)
        mSock.on("populate_chat_list", populateChatList)
    }

    var populateChatList = Emitter.Listener {
        val room: Rooms = gson.fromJson(it[0].toString(), Rooms::class.java)
        Log.d("ROOMS", room.toString())
        addToRecyclerView(room)
    }

    override fun onClick(p0: View?) {
       when(p0?.id) {
           R.id.join -> joinRoom()
           R.id.logoutButton -> logout()
       }
    }

    override fun onItemClick(position: Int) {
        val clickedItem = rooms[position]
        val uName = User.getUsername()
        val rName = clickedItem.room_name
        if(!rName.isNullOrBlank()&&!uName.isNullOrBlank()) {
            Toast.makeText(this, "Joining $rName", Toast.LENGTH_SHORT)
            val intent = Intent(this, ChatActvity::class.java)
            intent.putExtra("uName", uName)
            intent.putExtra("rName", rName)
            val data = Rooms("", rName, uName)
            val jData = gson.toJson(data)
            mSock.emit("joinRoom", jData)

            startActivity(intent)
        }
    }

    private fun logout() {
        Toast.makeText(this, "Logging out!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        Firebase.auth.signOut()
        startActivity(intent)
    }

    private fun joinRoom() {
        val uName = User.getUsername()
        val rName = rName.text.toString()

        if(!rName.isNullOrBlank()&&!uName.isNullOrBlank()) {
            val intent = Intent(this, ChatActvity::class.java)
            intent.putExtra("uName", uName)
            intent.putExtra("rName", rName)
            val data = Rooms("", rName, uName)
            val jData = gson.toJson(data)
            mSock.emit("joinRoom", jData)

            startActivity(intent)
        } else {
            Toast.makeText(this, "Please create or join a room", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToRecyclerView(room: Rooms) {
        runOnUiThread{
            Log.d(TAG, room.toString())
            rooms.add(room)
            Log.d(TAG, rooms.toString())
            Log.d(TAG, rooms.size.toString())
            chatRoomsActivityAdapter.notifyItemInserted(rooms.size)
//            recyclerView.scrollToPosition(rooms.size - 1)
        }
    }

}