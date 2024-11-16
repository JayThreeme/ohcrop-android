package com.ohc.ohcrop.howto.monitorandcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.ohc.ohcrop.Calculator
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.HowTo
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.databinding.ActivityMonitoringAndControlBinding
import com.ohc.ohcrop.databinding.ActivityMonitoringSensorsBinding

class MonitoringAndControl : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringAndControlBinding

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring_and_control)

        binding = ActivityMonitoringAndControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageBtnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

        binding.imageBtnBack.setOnClickListener {
            startActivity(Intent(this, HowTo::class.java))
            finish()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@MonitoringAndControl, HowTo::class.java)
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