package com.ohc.ohcrop.control.watertank

import android.R.bool
import android.app.ActivityOptions
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Switch
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.ohc.ohcrop.Calculator
import com.ohc.ohcrop.Control
import com.ohc.ohcrop.CropTracker
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.control.ControlDocumentUtils
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils
import kotlin.properties.Delegates

class WaterTankControl : AppCompatActivity() {

    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var controlswitch : Switch
    private lateinit var manualcontrolswitch : Switch

    private lateinit var manualCard : LinearLayout
    private lateinit var automaticCard : LinearLayout

    private lateinit var spinnerWaterLevel : Spinner
    private lateinit var setautomaticbutton : Button

    private lateinit var imagePhoto : ImageView

    private lateinit var userID: String
    private var controlsetting by Delegates.notNull<Boolean>()
    private lateinit var waterlevelstring: String
    private var waterlevels : Int = 0

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_tank_control)

        val enterAnimation = R.anim.slide_in_left
        val exitAnimation = R.anim.slide_out_right
        val options = ActivityOptions.makeCustomAnimation(this, enterAnimation, exitAnimation)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid

        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)

        controlswitch = findViewById(R.id.control_switch)
        manualcontrolswitch = findViewById(R.id.manual_switch)

        manualCard = findViewById(R.id.manual_card)
        automaticCard = findViewById(R.id.automatic_card)

        imagePhoto = findViewById(R.id.imagePhoto)

        //dropdown
        spinnerWaterLevel = findViewById(R.id.spinner_waterlevel)
        val waterlevels = arrayOf("none", "Level 1", "Level 2" ,"Level 3" ,"Level 4", "Level 5")
        val arraySpinner = ArrayAdapter(this@WaterTankControl, android.R.layout.simple_spinner_dropdown_item, waterlevels)
        spinnerWaterLevel.adapter = arraySpinner
        spinnerWaterLevel?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //toast("item selected" + waterlevels[position])
                waterlevelstring = waterlevels[position]
                setautomaticbutton.isVisible = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        //set automatic button
        setautomaticbutton = findViewById(R.id.setAutomaticBtn)
        setautomaticbutton.setOnClickListener {
            setAutomaticFun()
        }


        ControlDocumentUtils.createCollectionControl()
        ControlDocumentUtils.createWaterTankDocumentData()
        ControlDocumentUtils.createWaterTankDocument()
        SetControlSetting()

        controlswitch.setOnClickListener {
            controlswitchfun()
        }

        manualcontrolswitch.isChecked = false
        manualcontrolswitch.setOnClickListener {
            manualswitchfun()
            if(manualcontrolswitch.isChecked){
                manualcontrolswitch.setText("ON")
                imagePhoto.setImageResource(R.drawable.watertank)
            }else{
                manualcontrolswitch.setText("OFF")
                imagePhoto.setImageResource(R.drawable.watertankoff)
            }
        }

        ProfileImgButton.setOnClickListener {
            setmanualswitchfun()
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

        backButton.setOnClickListener {
            setmanualswitchfun()
            startActivity(Intent(this, Control::class.java), options.toBundle())
            finish()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@WaterTankControl, Control::class.java)
                startActivity(intent, options.toBundle())
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

    private fun setAutomaticFun() {
        if (waterlevelstring == "none"){
            waterlevels = 0
        }else if (waterlevelstring == "Level 1"){
            waterlevels = 1
        }else if (waterlevelstring == "Level 2"){
            waterlevels = 2
        }else if (waterlevelstring == "Level 3"){
            waterlevels = 3
        }else if (waterlevelstring == "Level 4"){
            waterlevels = 4
        }else if (waterlevelstring == "Level 5"){
            waterlevels = 5
        }
        FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("watertank").update("waterlevel", waterlevels)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }

        setautomaticbutton.isVisible = false
    }

    override fun onPause() {
        super.onPause()
        // Save control settings when activity is paused
        saveControlSettings()
    }

    override fun onResume() {
        super.onResume()
        // Load control settings when activity is resumed
        loadControlSettings()
    }

    // Function to save control settings
    private fun saveControlSettings() {
        val sharedPreferences = getSharedPreferences("ControlSettings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("controlSetting", controlsetting)
        editor.apply()
    }

    // Function to load control settings
    private fun loadControlSettings() {
        val sharedPreferences = getSharedPreferences("ControlSettings", Context.MODE_PRIVATE)
        controlsetting = sharedPreferences.getBoolean("controlSetting", false)
        updateUI(controlsetting)
    }

    private fun updateUI(controlMode: Boolean) {
        if (controlMode) {
            manualCard.visibility = View.GONE
            automaticCard.visibility = View.VISIBLE
            controlswitch.text = "Automatic Mode"
            controlswitch.isChecked = true
            setmanualswitchfun()
        } else {
            manualCard.visibility = View.VISIBLE
            automaticCard.visibility = View.GONE
            controlswitch.text = "Manual Mode"
            controlswitch.isChecked = false
            setmanualswitchfun()
        }
    }

    private fun manualswitchfun() {
        val switchstatus = manualcontrolswitch.isChecked

        FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("watertank").update("manualmode", switchstatus)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }
    }

    private fun setmanualswitchfun() {
        FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("watertank").update("manualmode", false)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }
        manualcontrolswitch.isChecked = false

    }



    private fun SetControlSetting() {
        val ref = FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("watertank")
        ref.get().addOnSuccessListener {
            if (it != null){
                controlsetting = it.data?.get("controlmode").toString().toBoolean()
                waterlevels = it.data?.get("waterlevel").toString().toInt()

                if (controlsetting){
                    imagePhoto.setImageResource(R.drawable.watertank)
                    manualCard.visibility = View.GONE
                    automaticCard.visibility = View.VISIBLE
                    controlswitch.text = "Automatic Mode"
                    controlswitch.isChecked = true
                    spinnerWaterLevel.setSelection(waterlevels)
                }else{
                    imagePhoto.setImageResource(R.drawable.watertankoff)
                    manualCard.visibility = View.VISIBLE
                    automaticCard.visibility = View.GONE
                    controlswitch.text = "Manual Mode"
                    controlswitch.isChecked = false
                }
            }else{
                Log.d(ContentValues.TAG, "Failed Getting Sowing Data")
            }
        }.addOnFailureListener {
                e -> Log.w(ContentValues.TAG, "Error writing document",e)
        }
    }

    private fun controlswitchfun() {
        val switchstatus = controlswitch.isChecked

        if (switchstatus){
            imagePhoto.setImageResource(R.drawable.watertank)
            manualCard.visibility = View.GONE
            automaticCard.visibility = View.VISIBLE
            controlswitch.text = "Automatic Mode"
            controlswitch.isChecked = true
            setmanualswitchfun()
        }else{
            imagePhoto.setImageResource(R.drawable.watertankoff)
            manualCard.visibility = View.VISIBLE
            automaticCard.visibility = View.GONE
            controlswitch.text = "Manual Mode"
            controlswitch.isChecked = false
        }

        FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("watertank").update("controlmode", switchstatus)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }
    }
}