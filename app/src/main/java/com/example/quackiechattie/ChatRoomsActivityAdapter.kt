/*
    Adapter for RecyclerView
 */
package com.example.quackiechattie

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class ChatRoomsActivityAdapter(val context: Context, val rooms: ArrayList<Rooms>, val listener: OnItemClickListener) : RecyclerView.Adapter<ChatRoomsActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("chatlist size", rooms.size.toString())
        var view: View? = null

        view = LayoutInflater.from(context).inflate(R.layout.row_room, parent, false)

        return ViewHolder(view!!)
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = rooms[position]
        val room_name = data.room_name
        Log.d("ROOM_NAME", room_name)
        holder.roomName.setText(room_name)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
        val roomName: TextView = itemView.findViewById(R.id.roomName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}