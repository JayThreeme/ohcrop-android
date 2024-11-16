package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.ohc.ohcrop.utils.FirebaseUtils

class LiveMonitoring : AppCompatActivity() {

    private lateinit var userID: String
    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var phBarChart: BarChart
    private lateinit var tdsBarChart: BarChart
    private lateinit var wlevelBarChart: BarChart
    private lateinit var wtempBarChart: BarChart
    private lateinit var humidityBarChart: BarChart
    private lateinit var airtempBarChart: BarChart

    private var phDataList = mutableListOf<Float>()
    private var tdsDataList = mutableListOf<Float>()
    private var waterlevelDataList = mutableListOf<Float>()
    private var watertempDataList = mutableListOf<Float>()
    private var humidityDataList = mutableListOf<Float>()
    private var airtempDataList = mutableListOf<Float>()

    private lateinit var progressbar: ProgressBar

    private val db = FirebaseUtils.firestore
    private lateinit var firestoreListener: ListenerRegistration
    private val getUserID = FirebaseUtils.firebaseAuth.currentUser!!.uid
    private var firebasedb = db.collection("user").document(getUserID).collection("monitor").document("result")
    private var previousSnapshot: DocumentSnapshot? = null
    private var initialFetchCompleted = false

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_monitoring)

        progressbar = findViewById(R.id.progress_bar)
        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)
        ProfileImgButton.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }
        //------------------------------------------------------
        phBarChart = findViewById(R.id.ph_chart)
        tdsBarChart = findViewById(R.id.tds_chart)
        wlevelBarChart = findViewById(R.id.waterlevel_chart)
        wtempBarChart = findViewById(R.id.watertemp_chart)
        humidityBarChart = findViewById(R.id.humidity_chart)
        airtempBarChart = findViewById(R.id.airtemp_chart)
        //------------------------------------------------------

        fetchDataOnce()

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@LiveMonitoring, Dashboard::class.java)
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

    private fun fetchDataOnce() {
        firebasedb.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    initialFetchCompleted = true
                    handleSnapshot(document)
                    registerFirestoreListener()
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }

    private fun registerFirestoreListener() {
        firestoreListener = firebasedb.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle any errors
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                if (previousSnapshot != null && snapshot != previousSnapshot) {
                    // Document has changed, check for field changes
                    checkFieldChanges(snapshot)
                }

                // Update the previous snapshot
                previousSnapshot = snapshot
            } else {
                // Document doesn't exist or is empty
                // Handle the case where no data is available
            }
        }
    }

    private fun checkFieldChanges(snapshot: DocumentSnapshot) {
        if (snapshot.contains("ph") && snapshot.getDouble("ph") != previousSnapshot?.getDouble("ph")) {
            val phData = snapshot.getDouble("ph")?.toFloat() ?: 0f
            phDataList.add(phData)
            updateBarChartWithData(phBarChart, phDataList, 1f, 15f, "ph Data", false)
        }

        if (snapshot.contains("tds") && snapshot.getDouble("tds") != previousSnapshot?.getDouble("tds")) {
            val tdsData = snapshot.getDouble("tds")?.toFloat() ?: 0f
            tdsDataList.add(tdsData)
            updateBarChartWithData(tdsBarChart, tdsDataList, 1f, 1000f, "tds Data", false)
        }

        if (snapshot.contains("water") && snapshot.getDouble("waterlevel") != previousSnapshot?.getDouble("waterlevel")) {
            val waterlevelData = snapshot.getDouble("waterlevel")?.toFloat() ?: 0f
            waterlevelDataList.add(waterlevelData)
            updateBarChartWithData(wlevelBarChart, waterlevelDataList, 1f, 15f, "water level Data", false)
        }

        if (snapshot.contains("watertemp") && snapshot.getDouble("watertemp") != previousSnapshot?.getDouble("watertemp")) {
            val watertempData = snapshot.getDouble("watertemp")?.toFloat() ?: 0f
            watertempDataList.add(watertempData)
            updateBarChartWithData(wtempBarChart, watertempDataList, 1f, 50f, "water temp Data", false)
        }

        if (snapshot.contains("humidity") && snapshot.getDouble("humidity") != previousSnapshot?.getDouble("humidity")) {
            val humidityData = snapshot.getDouble("humidity")?.toFloat() ?: 0f
            humidityDataList.add(humidityData)
            updateBarChartWithData(humidityBarChart, humidityDataList, 1f, 100f, "humidity Data", false)
        }

        if (snapshot.contains("airtemp") && snapshot.getDouble("airtemp") != previousSnapshot?.getDouble("airtemp")) {
            val airtempData = snapshot.getDouble("airtemp")?.toFloat() ?: 0f
            airtempDataList.add(airtempData)
            updateBarChartWithData(airtempBarChart, airtempDataList, 1f, 50f, "air temp Data", false)
        }
    }
    private fun handleSnapshot(snapshot: DocumentSnapshot) {
        if (snapshot.exists()) {
            // Handle the data if the document exists
            val ph = snapshot.getDouble("ph")?.toFloat() ?: 0f
            val tds = snapshot.getDouble("tds")?.toFloat() ?: 0f
            val waterlevel = snapshot.getDouble("water")?.toFloat() ?: 0f
            val watertemp = snapshot.getDouble("watertemp")?.toFloat() ?: 0f
            val humidity = snapshot.getDouble("humidity")?.toFloat() ?: 0f
            val airtemp = snapshot.getDouble("airtemp")?.toFloat() ?: 0f

            phDataList.add(ph)
            tdsDataList.add(tds)
            waterlevelDataList.add(waterlevel)
            watertempDataList.add(watertemp)
            humidityDataList.add(humidity)
            airtempDataList.add(airtemp)

            updateBarChartWithData(phBarChart, phDataList, 1f, 15f, "ph Data", false)
            updateBarChartWithData(tdsBarChart, tdsDataList, 1f, 1000f, "tds Data", false)
            updateBarChartWithData(wlevelBarChart, waterlevelDataList, 1f, 15f, "water level Data", false)
            updateBarChartWithData(wtempBarChart, watertempDataList, 1f, 50f, "water temp Data", false)
            updateBarChartWithData(humidityBarChart, humidityDataList, 1f, 100f, "humidity Data", false)
            updateBarChartWithData(airtempBarChart, airtempDataList, 1f, 50f, "air temp Data", false)
        } else {
            // Handle the case where the document doesn't exist
        }
    }

    private fun setupBarChart(barChart: BarChart, customMin: Float, customMax: Float, scalable: Boolean) {
        // Disable scrolling
        barChart.setScaleEnabled(scalable)

        // Customize X Axis
        val xAxis = barChart.xAxis
        xAxis.apply {
            isEnabled = true
            position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false) // Disable horizontal grid lines
        }

        // Customize Y Axis
        val leftYAxis = barChart.axisLeft
        leftYAxis.apply {
            isEnabled = true
            axisMinimum = customMin // Set the custom minimum value
            axisMaximum = customMax // Set the custom maximum value
            setDrawGridLines(false) // Disable horizontal grid lines
        }

        // Disable right Y Axis
        barChart.axisRight.apply {
            isEnabled = false
            setDrawGridLines(false) // Disable horizontal grid lines
        }

        // Additional customizations can be added here
        progressbar.visibility = View.GONE
    }

    private fun updateBarChartWithData(barChart: BarChart, data: List<Float>, customMin: Float, customMax: Float, title: String, scalable: Boolean) {
        val entries = mutableListOf<BarEntry>()
        data.forEachIndexed { index, value ->
            entries.add(BarEntry(index.toFloat(), value))
        }
        val dataSet = BarDataSet(entries, title)
        dataSet.color = ContextCompat.getColor(this, R.color.textcolor_blue)

        val barData = BarData(dataSet)

        barChart.apply {
            this.data = barData
            description.text = ""
            description.isEnabled = true

            setupBarChart(barChart, customMin, customMax, scalable)

            // Customize X Axis specific to this chart
            val xAxis = xAxis
            xAxis.apply {
                setLabelCount(0, true) // Disable the labels
                valueFormatter = null // Clear the value formatter
            }
            animateY(1000)
            invalidate()
        }
    }

    private fun unregisterFirestoreListener() {
        // Unregister Firestore listener
        firestoreListener.remove()
    }

    override fun onStart() {
        super.onStart()
        // Register Firestore listener
        //registerFirestoreListener()
    }

    override fun onStop() {
        super.onStop()
        unregisterFirestoreListener()
    }
}