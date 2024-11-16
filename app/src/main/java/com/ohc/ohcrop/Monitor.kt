package com.ohc.ohcrop

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ohc.ohcrop.databinding.ActivityMonitorBinding
import com.ohc.ohcrop.utils.FirebaseUtils
import com.ohc.ohcrop.utils.FirebaseUtils.firestore
import java.math.RoundingMode
import java.text.DecimalFormat


class Monitor : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var binding: ActivityMonitorBinding
    private lateinit var database: DatabaseReference

    private lateinit var backButton: ImageButton
    private lateinit var profileButton: ImageButton

    private lateinit var userID: String

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        binding = ActivityMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid

        databaseListener() //call function
        statusListener()

        binding.imageBtnBack.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        binding.imageBtnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@Monitor, Dashboard::class.java)
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

    private fun statusListener() {
        val ref = firestore.collection("user").document(userID).collection("setting").document("default")
        ref.get().addOnSuccessListener {
            if (it != null){
                val devicestatus: Boolean = it.data?.get("devicestatus").toString().toBoolean()
                val ssid  = it.data?.get("devicessid").toString()
                val ip  = it.data?.get("deviceip").toString()

                binding.monitorssidtext.setText("Device SSID: $ssid")
                binding.monitoriptext.setText("Device IP: $ip")

                if (devicestatus){
                    binding.monitordevicetext.setText("OhCrop Device Status: ON")
                }else{
                    binding.monitordevicetext.setText("OhCrop Device Status: OFF")
                }

            }else{
                Log.d(ContentValues.TAG, "Failed Getting Sowing Data")
            }
        }
        .addOnFailureListener {
                e -> Log.w(ContentValues.TAG, "Error writing document",e)
        }
    }

    private fun databaseListener() {
        database = FirebaseDatabase.getInstance().getReference()
        val postListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
            override fun onDataChange(snapshot: DataSnapshot) {
                val watertemp = snapshot.child("$userID/Monitor/watertemp").value
                val waterlevel = snapshot.child("$userID/Monitor/water").value
                val phlevel = snapshot.child("$userID/Monitor/ph").value
                val humidity = snapshot.child("$userID/Monitor/humidity").value
                val airtemp = snapshot.child("$userID/Monitor/airtemp").value
                val tds = snapshot.child("$userID/Monitor/tds").value

                //val ph_level = (phlevel.toString().toDouble() * 10000.0).roundToInt() / 10000.0
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.DOWN
                val ph_level = df.format(phlevel.toString().toDouble())
                println(ph_level)
                //val valueType = airtemp?.javaClass?.simpleName ?: "Unknown"
                //binding.textOtherResult.setText("String Value: $vdouble")


                if (watertemp.toString() != null && waterlevel.toString() != null){
                    binding.textWaterTemperature.setText(watertemp.toString().toDouble().toInt().toString()+"°C")
                    binding.progressbarWaterLevel.setProgress(waterlevel.toString().toDouble().toInt(), true)
                }else{

                }

                if (phlevel.toString() != null){
                    binding.progressbarPhLevel.setProgress(phlevel.toString().toDouble().toInt(), true)
                    binding.textPhlevel.setText(ph_level.toString()+"")
                }else{

                }

                if (humidity.toString() != null){
                    binding.progressbarHumidity.setProgress(Integer.parseInt(humidity.toString()), true)
                    binding.textHumidity.setText(humidity.toString()+"%")
                }else{

                }

                if (airtemp.toString() != null){
                    binding.progressbarAirTemp.setProgress(airtemp.toString().toDouble().toInt(), true)
                    binding.textAirtemp.setText(airtemp.toString()+"°C")
                }else{

                }

                if (tds.toString()!= null){
                    binding.progressbarTdslevel.setProgress(tds.toString().toDouble().toInt(), true)
                    binding.textTdsLevel.setText(tds.toString().toDouble().toInt().toString()+"ppm")
                }else{

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Monitor, "Failed to read data", Toast.LENGTH_SHORT).show()
            }
        }
        database.addValueEventListener(postListener)
    }

}