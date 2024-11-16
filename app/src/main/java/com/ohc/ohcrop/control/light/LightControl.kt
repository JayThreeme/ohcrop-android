package com.ohc.ohcrop.control.light

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
import com.ohc.ohcrop.Control
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.control.ControlDocumentUtils
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils
import kotlin.properties.Delegates

class LightControl : AppCompatActivity() {

    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var controlswitch : Switch
    private lateinit var manualcontrolswitch : Switch

    private lateinit var manualCard : LinearLayout
    private lateinit var automaticCard : LinearLayout

    private lateinit var imagePhoto : ImageView

    //automatic items
    private lateinit var spinnerfrom : Spinner
    private lateinit var spinnerto : Spinner
    private lateinit var spinnerduratoion : Spinner
    private lateinit var setautomaticbutton : Button
    private lateinit var spinnerFrom: String
    private lateinit var spinnerTo: String
    private lateinit var spinnerDuration: String
    private  var spinnerFromPosition: Int = 0
    private  var spinnerToPosition: Int = 0
    private  var spinnerDurationPosition: Int = 0
    //automatic items

    private lateinit var userID: String
    private var controlsetting by Delegates.notNull<Boolean>()

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_control)

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

        ControlDocumentUtils.createCollectionControl()
        ControlDocumentUtils.createLightDocument()
        SetControlSetting()

        //automatic items-----------------------------------
        spinnerfrom = findViewById(R.id.spinner_from)
        spinnerfromfun()
        spinnerto = findViewById(R.id.spinner_to)
        spinnertofun()
        spinnerduratoion = findViewById(R.id.spinner_duration)
        spinnerdurationfun()
        setautomaticbutton = findViewById(R.id.automatic_btn)
        setautomaticbutton.setOnClickListener {
            setAutomaticFun()
        }
        setautomaticsettings()
        //--------------------------------------------------

        controlswitch.setOnClickListener {
            controlswitchfun()
        }

        manualcontrolswitch.isChecked = false
        manualcontrolswitch.setOnClickListener {
            manualswitchfun()
            if(manualcontrolswitch.isChecked){
                manualcontrolswitch.setText("ON")
                imagePhoto.setImageResource(R.drawable.light)
            }else{
                manualcontrolswitch.setText("OFF")
                imagePhoto.setImageResource(R.drawable.lightoff)
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
                val intent = Intent(this@LightControl, Control::class.java)
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

    // automatic ------------------------------------------------------
    private fun spinnerfromfun() {
        val spinneritems = arrayOf("none","7 AM","8 AM","9 AM","10 AM","11 AM","12 AM","1 PM","2 PM","3 PM","4 PM","5 PM","6 PM")
        val arraySpinner = ArrayAdapter(this@LightControl, android.R.layout.simple_spinner_dropdown_item, spinneritems)
        spinnerfrom.adapter = arraySpinner
        spinnerfrom?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //toast("item selected" + spinneritems[position])
                spinnerFrom = spinneritems[position]
                spinnerFromPosition = position
                setautomaticbutton.isVisible = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private fun spinnertofun() {
        val spinneritems = arrayOf("none","7 AM","8 AM","9 AM","10 AM","11 AM","12 AM","1 PM","2 PM","3 PM","4 PM","5 PM","6 PM")
        val arraySpinner = ArrayAdapter(this@LightControl, android.R.layout.simple_spinner_dropdown_item, spinneritems)
        spinnerto.adapter = arraySpinner
        spinnerto?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //toast("item selected" + spinneritems[position])
                spinnerTo = spinneritems[position]
                spinnerToPosition = position
                setautomaticbutton.isVisible = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun spinnerdurationfun() {
        val spinneritems = arrayOf("none","5min","10 min", "15 min", "30 min", "45 min", "60 min")
        val arraySpinner = ArrayAdapter(this@LightControl, android.R.layout.simple_spinner_dropdown_item, spinneritems)
        spinnerduratoion.adapter = arraySpinner
        spinnerduratoion?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //toast("item selected" + spinneritems[position])
                spinnerDuration = spinneritems[position]
                spinnerDurationPosition = position
                setautomaticbutton.isVisible = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setAutomaticFun() {
        var from = arrayOf(0,7,8,9,10,11,12,13,14,15,16,17,18,19)
        var to = arrayOf(0,7,8,9,10,11,12,13,14,15,16,17,18,19)
        var duration = arrayOf(0,5,10,15,30,45,60)

        if (spinnerFromPosition == 0 || spinnerToPosition == 0 || spinnerDurationPosition == 0) {
            toast("Error: Set you parameters")
        }else{
            if (spinnerFromPosition <= spinnerToPosition){
                FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("light").update(
                    mapOf(
                        "from" to from[spinnerFromPosition],
                        "to" to to[spinnerToPosition],
                        "duration" to duration[spinnerDurationPosition],
                        "indexFrom" to spinnerFromPosition,
                        "indexTo" to spinnerToPosition,
                        "indexDuration" to spinnerDurationPosition,
                    )
                )
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error writing document", e)
                    }
                setautomaticbutton.isVisible = false
            }else{
                toast("Error: Your Time From is less than from time to")
            }
        }
    }

    private fun setautomaticsettings(){
        val ref = FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("light")
        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val data = documentSnapshot.data
                if (data != null) {
                    if (data.contains("indexFrom") && data.contains("indexTo") && data.contains("indexDuration")) {
                        spinnerFromPosition = data["indexFrom"].toString().toInt()
                        spinnerToPosition = data["indexTo"].toString().toInt()
                        spinnerDurationPosition = data["indexDuration"].toString().toInt()

                        spinnerfrom.setSelection(spinnerFromPosition)
                        spinnerto.setSelection(spinnerToPosition)
                        spinnerduratoion.setSelection(spinnerDurationPosition)
                    } else {
                        Log.d(ContentValues.TAG, "One or more fields are missing")
                    }
                } else {
                    Log.d(ContentValues.TAG, "Data is null")
                }
            } else {
                Log.d(ContentValues.TAG, "Document does not exist")
            }
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error reading document", e)
        }
    }

    // automatic ----------------------------------------------------------


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
        FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("light").update("manualmode", switchstatus)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }
    }

    private fun setmanualswitchfun() {
        FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("light").update("manualmode", false)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }
        manualcontrolswitch.isChecked = false
    }



    private fun SetControlSetting() {
        val ref = FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("light")
        ref.get().addOnSuccessListener {
            if (it != null){
                controlsetting = it.data?.get("controlmode").toString().toBoolean()

                if (controlsetting){
                    imagePhoto.setImageResource(R.drawable.light)
                    manualCard.visibility = View.GONE
                    automaticCard.visibility = View.VISIBLE
                    controlswitch.text = "Automatic Mode"
                    controlswitch.isChecked = true
                }else{
                    imagePhoto.setImageResource(R.drawable.lightoff)
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
            imagePhoto.setImageResource(R.drawable.light)
            manualCard.visibility = View.GONE
            automaticCard.visibility = View.VISIBLE
            controlswitch.text = "Automatic Mode"
            controlswitch.isChecked = true
            setmanualswitchfun()
        }else{
            imagePhoto.setImageResource(R.drawable.lightoff)
            manualCard.visibility = View.VISIBLE
            automaticCard.visibility = View.GONE
            controlswitch.text = "Manual Mode"
            controlswitch.isChecked = false
        }

        FirebaseUtils.firestore.collection("user").document(userID).collection("control").document("light").update("controlmode", switchstatus)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }
    }
}