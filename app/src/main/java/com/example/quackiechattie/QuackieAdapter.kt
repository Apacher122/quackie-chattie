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


class QuackieAdapter(val context: Context, val chats: ArrayList<Chat>) : RecyclerView.Adapter<QuackieAdapter.ViewHolder>() {
    val SENT = 0
    val RECV = 1
    val JOIN = 2
    val LEFT = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("chatlist size", chats.size.toString())
        var view: View? = null
        when (viewType) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.row_me, parent, false)
                Log.d("me inflating","viewType : ${viewType}")
            }
            1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.row_you, parent, false)
                Log.d("you inflating", "viewType : ${viewType}")
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.notifs, parent, false)
                Log.d("join or leave", "viewType : ${viewType}")
            }
            3 -> {
                view = LayoutInflater.from(context).inflate(R.layout.notifs, parent, false)
                Log.d("join or leave", "viewType : ${viewType}")
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
        val uName = data.uName;
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