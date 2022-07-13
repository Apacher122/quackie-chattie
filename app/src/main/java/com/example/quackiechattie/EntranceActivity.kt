/*
    For usernames and rooms to enter
 */

package com.example.quackiechattie

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast


import kotlinx.android.synthetic.main.activity_menu.*



class EntranceActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        button.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.button -> joinRoom()
        }
    }

    private fun joinRoom() {
        val uName = uName.text.toString()
        val rName = rName.text.toString()

        if(!rName.isNullOrBlank()&&!uName.isNullOrBlank()) {
            val intent = Intent(this, QuackieActivity::class.java)
            intent.putExtra("uName", uName)
            intent.putExtra("rName", rName)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please enter name and room", Toast.LENGTH_SHORT)
        }
    }
}