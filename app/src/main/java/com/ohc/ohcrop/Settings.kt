package com.ohc.ohcrop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import com.ohc.ohcrop.databinding.ActivityMasterblendBinding
import com.ohc.ohcrop.databinding.ActivitySettingsBinding
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils
import com.ohc.ohcrop.utils.FirebaseUtils.firestore

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private lateinit var userID: String

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid

        binding.imageBtnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

        binding.imageBtnBack.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        binding.dashboardLogoutBtn.setOnClickListener {
            FirebaseUtils.firebaseAuth.signOut()
            startActivity(Intent(this, Login::class.java))
            toast("signed out")
            finish()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@Settings, Dashboard::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        getHardwareInfo(userID)


        binding.otherbutton.setOnClickListener {
            startActivity(Intent(this, AddCrops::class.java))
            finish()
        }
    }

    private fun getHardwareInfo(userID: String) {
        binding.progressBar.visibility = View.VISIBLE
        firestore.collection("user")
            .document(userID)
            .collection("setting")
            .document("default")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Access the fields from the document
                    val date = document.getString("date")
                    val deviceIP = document.getString("deviceip")
                    val deviceSSID = document.getString("devicessid")
                    val deviceStatus = document.getBoolean("devicestatus")

                    // Process the hardware information as needed
                    binding.hardwareinfo.setText(("Date: $date\nDevice IP: $deviceIP\nDevice SSID: $deviceSSID\nDevice Status: $deviceStatus").toString())
                } else {
                    // Document doesn't exist
                }
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }

    }


    override fun onDestroy() {
        super.onDestroy()
        // Remove callback when activity is destroyed
        onBackPressedCallback.remove()
    }
}