package com.ohc.ohcrop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ohc.ohcrop.calculator.RecyclerCalcAdapter
import com.ohc.ohcrop.control.RecyclerControlAdapter
import com.ohc.ohcrop.utils.Extensions.toast

class Calculator : AppCompatActivity() {

    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerCalcAdapter.ViewHolder>? = null
    private lateinit var recyclerview: RecyclerView

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        //header
        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)

        layoutManager = LinearLayoutManager(this)

        // getting the recyclerview by its id
        recyclerview = findViewById(R.id.calculator_recyclerView)
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter = RecyclerCalcAdapter()
        recyclerview.adapter = adapter

        //profile and back button listener
        ProfileImgButton.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            //toast("Profile")
            finish()
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            //toast("Crop yield")
            finish()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@Calculator, Dashboard::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }
    override fun onDestroy() {
        super.onDestroy()
        // Remove callback when activity is destroyed
        onBackPressedCallback.remove()
    }
}