package com.ohc.ohcrop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.ohc.ohcrop.reports.ChartChoice


class Reports : AppCompatActivity() {

    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var phlevel_btn : Button
    private lateinit var tds_btn : Button
    private lateinit var watertemp_btn : Button
    private lateinit var waterlvel_btn : Button
    private lateinit var humidity_btn : Button
    private lateinit var airtemp_btn : Button

    // THIS IS NOT IN USE
    // THIS IS NOT IN USE
    // THIS IS NOT IN USE
    // THIS IS NOT IN USE
    // THIS IS NOT IN USE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)

        phlevel_btn = findViewById(R.id.rBtn_PH)
        tds_btn = findViewById(R.id.rBtn_TDS)
        watertemp_btn = findViewById(R.id.rBtn_WaterTemp)
        waterlvel_btn = findViewById(R.id.rBtn_Water)
        humidity_btn = findViewById(R.id.rBtn_Humidity)
        airtemp_btn = findViewById(R.id.rBtn_AirTemp)

        phlevel_btn.setOnClickListener {
            val intent = Intent(this, ChartChoice::class.java)
            intent.putExtra("reports","ph")
            this.startActivity(intent)
            finish()
        }
        tds_btn.setOnClickListener {
            val intent = Intent(this, ChartChoice::class.java)
            intent.putExtra("reports","tds")
            this.startActivity(intent)
            finish()
        }
        waterlvel_btn.setOnClickListener {
            val intent = Intent(this, ChartChoice::class.java)
            intent.putExtra("reports","waterlevel")
            this.startActivity(intent)
            finish()
        }
        watertemp_btn.setOnClickListener {
            val intent = Intent(this, ChartChoice::class.java)
            intent.putExtra("reports","watertemp")
            this.startActivity(intent)
            finish()
        }
        humidity_btn.setOnClickListener {
            val intent = Intent(this, ChartChoice::class.java)
            intent.putExtra("reports","humidity")
            this.startActivity(intent)
            finish()
        }
        airtemp_btn.setOnClickListener {
            val intent = Intent(this, ChartChoice::class.java)
            intent.putExtra("reports","temperature")
            this.startActivity(intent)
            finish()
        }




        ProfileImgButton.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

    }
}