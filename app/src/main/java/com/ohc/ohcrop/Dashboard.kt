package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.ohc.ohcrop.reports.datareports.DataReports
import com.ohc.ohcrop.utils.FirebaseUtils

class Dashboard : AppCompatActivity() {

    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var monitorButton : Button
    private lateinit var cropTrack : Button
    private lateinit var controlButton : Button
    private lateinit var reportButton : Button
    private lateinit var liveButton : Button
    private lateinit var howToButton : Button
    private lateinit var calculatorButton : Button
    private lateinit var settingsButton : Button


    private var mFirebaseAnalytics: FirebaseAnalytics? = null


    private lateinit var userID: String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)

        monitorButton = findViewById(R.id.dashboardMonitorBtn)
        cropTrack = findViewById(R.id.dashboardCropTrack)
        controlButton = findViewById(R.id.dashboardControlBtn)
        reportButton = findViewById(R.id.dashboardReportBtn)
        liveButton = findViewById(R.id.dashboardLiveBtn)
        howToButton = findViewById(R.id.dashboardHowToBtn)
        calculatorButton  = findViewById(R.id.dashboardCalcBtn)
        settingsButton = findViewById(R.id.dashboardSettingsBtn2)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid


        monitorButton.setOnClickListener {
            startActivity(Intent(this, Monitor::class.java))
            //toast("Monitor System")
            finish()
        }
        controlButton.setOnClickListener {
            startActivity(Intent(this, Control::class.java))
            //toast("Crop Track")
            finish()
        }
        cropTrack.setOnClickListener {
            startActivity(Intent(this, CropTracker::class.java))
            //toast("Crop Track")
            finish()
        }
        reportButton.setOnClickListener {
            startActivity(Intent(this, DataReports::class.java))
            //toast("Crop Track")
            finish()
        }
        liveButton.setOnClickListener {
            startActivity(Intent(this, LiveMonitoring::class.java))
            //toast("Crop Track")
            finish()
        }

        howToButton.setOnClickListener {
            startActivity(Intent(this, HowTo::class.java))
            //toast("Know How")
            finish()
        }

        calculatorButton.setOnClickListener {
            startActivity(Intent(this, Calculator::class.java))
            //toast("Monitor System")
            finish()
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
            //toast("Monitor System")
            finish()
        }

        ProfileImgButton.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

    }
}