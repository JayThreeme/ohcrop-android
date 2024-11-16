package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ohc.ohcrop.calculator.RecyclerCalcAdapter
import com.ohc.ohcrop.croptrack.cropTrackAdapter
import com.ohc.ohcrop.croptrack.croptrackeradd.cropTrackerAdd

class CropTracker : AppCompatActivity() {

    private lateinit var userID: String
    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton
    private lateinit var addButton : Button
    private lateinit var progressBar : ProgressBar

    private lateinit var recyclerView: RecyclerView
    private var adapter: cropTrackAdapter? = null

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_tracker)

        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)
        addButton = findViewById(R.id.add_button)
        progressBar = findViewById(R.id.progress_bar)

        ProfileImgButton.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }
        addButton.setOnClickListener {
            startActivity(Intent(this, cropTrackerAdd::class.java))
            finish()
        }

        recyclerView = findViewById(R.id.croptracker_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter
        adapter = cropTrackAdapter()

        // Set the adapter to the RecyclerView
        recyclerView.adapter = adapter

        // Initially, show the progress bar
        adapter?.showProgressBar()

        // Perform data fetching
        fetchDataFromFirestore()

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@CropTracker, Dashboard::class.java)
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

    private fun fetchDataFromFirestore() {
        recyclerView.postDelayed({
            adapter?.hideProgressBar()
            progressBar.visibility = View.GONE
        }, 2000)

    }
}