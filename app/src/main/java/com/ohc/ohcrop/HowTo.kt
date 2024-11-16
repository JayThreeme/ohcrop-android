package com.ohc.ohcrop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ohc.ohcrop.howto.CustomAdapter
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.howto.ItemsViewModel

class HowTo : AppCompatActivity() {

    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var recyclerview: RecyclerView

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howto)

        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)

        // getting the recyclerview by its id
        recyclerview = findViewById(R.id.howToRecyclerView)
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()
        // This loop will create 20 Views containing
        // the image with the count of view
//        for (i in 1..10) {
//            data.add(ItemsViewModel(R.drawable.howto_baseline_folder_24, "Item " + i))
//        }
        data.add(ItemsViewModel(R.drawable.import_web_analytics, "OhCrop Monitoring & Control"))
        data.add(ItemsViewModel(R.drawable.import_metering, "Ohcrop Monitoring Sensors"))
        data.add(ItemsViewModel(R.drawable.import_travel, "OhCrop App Navigation"))
        data.add(ItemsViewModel(R.drawable.import_hydroponic, "What is Hydroponic"))
        data.add(ItemsViewModel(R.drawable.import_lettuce, "Hydroponic Crops"))
        data.add(ItemsViewModel(R.drawable.import_hydroponic_system, "Hydroponic Systems"))
        //data.add(ItemsViewModel(R.drawable.howto_baseline_folder_24, "....."))


        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data, this)
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        ProfileImgButton.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@HowTo, Dashboard::class.java)
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