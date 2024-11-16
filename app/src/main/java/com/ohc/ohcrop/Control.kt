package com.ohc.ohcrop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.control.RecyclerControlAdapter


class Control : AppCompatActivity() {


    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerControlAdapter.ViewHolder>? = null
    private lateinit var recyclerview: RecyclerView

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        //header
        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)

        layoutManager = LinearLayoutManager(this)

        // getting the recyclerview by its id
        recyclerview = findViewById(R.id.control_recyclerView)
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter = RecyclerControlAdapter()
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
                val intent = Intent(this@Control, Dashboard::class.java)
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