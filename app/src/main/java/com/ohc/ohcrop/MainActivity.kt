package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testbtn = findViewById<Button>(R.id.MainBtnTest)//Test Button to Test views
        testbtn.setOnClickListener {
            val intent = Intent(this, Test::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext,"Test ESP32 + RTDB + Mobile App", Toast.LENGTH_SHORT).show()
        }

        val dashboardBtn = findViewById<Button>(R.id.MainBtnDashboard)//Test Button to Test views
        dashboardBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext,"Test ESP32 + RTDB + Mobile App", Toast.LENGTH_SHORT).show()
        }


    }
}