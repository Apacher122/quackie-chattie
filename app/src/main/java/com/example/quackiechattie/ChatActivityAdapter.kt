package com.example.quackiechattie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatActivityAdapter(val context: Context, val chats: ArrayList<Chat>) : RecyclerView.Adapter<ChatActivityAdapter.ViewHolder>() {
    val SENT = 0
    val RECV = 1
    val JOIN = 2
    val LEFT = 3
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        when (viewType) {
            SENT -> {
                view = LayoutInflater.from(context).inflate(R.layout.row_me, parent, false)
            }
            RECV -> {
                view = LayoutInflater.from(context).inflate(R.layout.row_you, parent, false)
            }
            JOIN -> {
                view = LayoutInflater.from(context).inflate(R.layout.notifs, parent, false)
            }
            LEFT -> {
                view = LayoutInflater.from(context).inflate(R.layout.notifs, parent, false)
            }
        }

        return ViewHolder(view!!)
    }

    override fun getItemCount(): Int {
       return chats.size
    }

    override fun getItemViewType(position: Int): Int {
        return chats[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = chats[position]
        val uName = data.uName
        val content = data.content;
        val viewType = data.viewType;

        when(viewType) {
            SENT -> {
                holder.msg.setText(content)
            }
            RECV -> {
                holder.uName.setText(uName)
                holder.msg.setText(content)
            }
            JOIN -> {
                val temp = "${uName} quacked in"
                holder.text.setText(temp)
            }
            LEFT -> {
                val temp = "${uName} quacked out"
                holder.text.setText(temp)
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val uName = itemView.findViewById<TextView>(R.id.uName)
        val msg = itemView.findViewById<TextView>(R.id.message)
        val text = itemView.findViewById<TextView>(R.id.text)
    }
}