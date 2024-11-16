package com.ohc.ohcrop.reports.datareports

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.ohc.ohcrop.Control
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataReports : AppCompatActivity() {
    private lateinit var userID: String
    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var progressbar: ProgressBar
    private lateinit var setDateButton: Button
    private lateinit var showButton: Button
    private lateinit var showButtonWeekMonth: Button
    private lateinit var showDateTextView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioDaily: RadioButton
    private lateinit var radioWeekly: RadioButton
    private lateinit var radioMonthly: RadioButton

    private lateinit var linearLayoutMonth: LinearLayout
    private lateinit var linearLayoutWeek: LinearLayout
    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerWeek: Spinner

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

    private val daysOfTheWeek = mutableListOf<Int>()
    private val datesOfTheWeek = mutableListOf<String>()
    private var weeklyDataLists = mutableListOf<MutableList<Triple<String, Int, Float>>>() // Initial empty list

    private var phDataListWeekMonth = mutableListOf<Pair<String, Float>>()
    private var tdsDataListWeekMonth = mutableListOf<Pair<String, Float>>()
    private var waterlevelDataListWeekMonth = mutableListOf<Pair<String, Float>>()
    private var watertempDataListWeekMonth = mutableListOf<Pair<String, Float>>()
    private var humidityDataListWeekMonth = mutableListOf<Pair<String, Float>>()
    private var airtempDataListWeekMonth = mutableListOf<Pair<String, Float>>()

    private var phmax: Float = 0.0f
    private var tdsmax: Float = 0.0f
    private var waterlevelmax: Float = 0.0f
    private var watertempmax: Float = 0.0f
    private var humiditymax: Float = 0.0f
    private var airtempmax: Float = 0.0f

    private val calendar = Calendar.getInstance()

    private lateinit var DateToday: LocalDate
    private  var selectedMonth: Int = 0
    private  var selectedWeek: Int = 0
    private  var selectedYear: Int = 0

    private lateinit var TestText: TextView

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_reports)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid
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
        //--------------------------------------------------
        progressbar = findViewById(R.id.progress_bar)
        setDateButton = findViewById(R.id.selectDateButton)
        showButton = findViewById(R.id.showButton)
        showDateTextView = findViewById(R.id.textView_date)
        radioGroup = findViewById(R.id.radiogroup)
        radioDaily = findViewById(R.id.radioDaily)
        radioWeekly = findViewById(R.id.radioWeekly)
        radioMonthly = findViewById(R.id.radioMonthly)

        phBarChart = findViewById(R.id.ph_chart)
        tdsBarChart = findViewById(R.id.tds_chart)
        wlevelBarChart = findViewById(R.id.waterlevel_chart)
        wtempBarChart = findViewById(R.id.watertemp_chart)
        humidityBarChart = findViewById(R.id.humidity_chart)
        airtempBarChart = findViewById(R.id.airtemp_chart)

        showButtonWeekMonth = findViewById(R.id.showButton_WeekMonth)
        linearLayoutMonth = findViewById(R.id.month_selection_layout)
        linearLayoutWeek = findViewById(R.id.week_selection_layout)
        spinnerMonth = findViewById(R.id.spinner_Month)
        spinnerWeek = findViewById(R.id.spinner_Week)
        linearLayoutMonth.visibility = View.GONE
        linearLayoutWeek.visibility = View.GONE
        //--------------------------------------------------
        TestText = findViewById(R.id.textview_test)//for testing output text

        getDateToday()//get local date today

        getDataFromFirebase(object : DataRetrievalCallback {
            override fun onDataRetrieved() {
                dailyfunChart()
            }
        })

        radioDaily.isChecked = true
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioDaily -> {
                   //toast("Daily")
                    linearLayoutMonth.visibility = View.GONE
                    linearLayoutWeek.visibility = View.GONE
                    setDateButton.visibility = View.VISIBLE
                    showButtonWeekMonth.visibility = View.GONE
                }
                R.id.radioWeekly -> {
                    //toast("Weekly")
                    spinnerMonth.setSelection(selectedMonth)
                    spinnerweekfun()
                    spinnermonthfun()
                    linearLayoutMonth.visibility = View.VISIBLE
                    linearLayoutWeek.visibility = View.VISIBLE
                    setDateButton.visibility = View.GONE
                    showButtonWeekMonth.visibility = View.GONE
                    showButton.visibility = View.GONE
                    showDateTextView.text = "Date: "

                }
                R.id.radioMonthly -> {
                    //toast("Monthly")
                    spinnermonthfun()
                    linearLayoutMonth.visibility = View.VISIBLE
                    linearLayoutWeek.visibility = View.GONE
                    setDateButton.visibility = View.GONE
                    showButtonWeekMonth.visibility = View.GONE
                    showButton.visibility = View.GONE
                    showDateTextView.text = "Date: "
                }
            }
        }

        showButtonWeekMonth.visibility = View.GONE
        showButtonWeekMonth.setOnClickListener {
            if (radioWeekly.isChecked){
                populateDaysOfTheWeek()
                getFirebaseWeekMonth(datesOfTheWeek)
                showButtonWeekMonth.visibility = View.GONE
                showButton.visibility = View.GONE
                showDateTextView.text = "Date: (" + datesOfTheWeek.firstOrNull() + " to " + datesOfTheWeek.lastOrNull() +")"

                TestText.text = "selectedweek:" +selectedWeek.toString() + "selectedMonth:" + selectedMonth.toString() + phDataListWeekMonth.toString()
            }else if (radioMonthly.isChecked){
                populateDaysOfTheMonth()
                getFirebaseWeekMonth(datesOfTheWeek)
                showButtonWeekMonth.visibility = View.GONE
                showButton.visibility = View.GONE
                showDateTextView.text = "Date: (" + datesOfTheWeek.firstOrNull() + " to " + datesOfTheWeek.lastOrNull()+")"

                TestText.text = "selectedweek:" +selectedWeek.toString() + "selectedMonth:" + selectedMonth.toString() + phDataListWeekMonth.toString()
            }else{
                toast("select something")
            }
        }

        setDateButton.setOnClickListener {
            setDatefun()
            //showWeekDatePickerDialog()
        }

        showButton.visibility = View.GONE
        showButton.setOnClickListener {
            showButtonfun()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@DataReports, Dashboard::class.java)
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

    fun getFirebaseWeekMonth(dates: List<String>) {
        progressbar.visibility = View.VISIBLE

        phDataListWeekMonth.clear()
        tdsDataListWeekMonth.clear()
        waterlevelDataListWeekMonth.clear()
        watertempDataListWeekMonth.clear()
        humidityDataListWeekMonth.clear()
        airtempDataListWeekMonth.clear()

        val db = FirebaseUtils.firestore
        val userReportsRef = db.collection("user").document(userID).collection("report")
        for (date in dates) {
            val query = userReportsRef.whereEqualTo("date", date)

            query.get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    val temporaryDataLists = mutableListOf(
                        mutableListOf<Pair<Int, Float>>(), // pH
                        mutableListOf<Pair<Int, Float>>(), // TDS
                        mutableListOf<Pair<Int, Float>>(), // Water level
                        mutableListOf<Pair<Int, Float>>(), // Water temp
                        mutableListOf<Pair<Int, Float>>(), // Humidity
                        mutableListOf<Pair<Int, Float>>()) // Air temp
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            //val reportData = document.data
                            //println("Report data for date $date: $reportData")
                            val ph = document.getDouble("ph")?.toFloat()
                            val tds = document.getDouble("tds")?.toFloat()
                            val waterlevel = document.getDouble("waterlevel")?.toFloat()
                            val watertemp = document.getDouble("watertemp")?.toFloat()
                            val humidity = document.getDouble("humidity")?.toFloat()
                            val airtemp = document.getDouble("temperature")?.toFloat()
                            val hour = document.getDouble("hour")?.toInt()

                            val data = listOf(hour to ph, hour to tds, hour to waterlevel, hour to watertemp, hour to humidity, hour to airtemp)

                            data.forEachIndexed { index, triple ->
                                if (hour != null && triple.second != null) {
                                    temporaryDataLists[index].add(hour to triple.second!!)
                                }
                            }
                            // private var weeklyDataLists = mutableListOf<MutableList<Triple<String, Int, Float>>>() // Initial empty list
                        }
                        temporaryDataLists.forEach { dataList ->
                            dataList.sortBy { it.first }
                        }

                        // Fill the gaps of missing time
                        val filledDataLists = temporaryDataLists.map { dataList ->
                            val filledDataList = mutableListOf<Pair<Int, Float>>()
                            var currentHour = 7
                            var dataIndex = 0

                            while (currentHour <= 18) {
                                if (dataIndex < dataList.size && dataList[dataIndex].first == currentHour) {
                                    filledDataList.add(dataList[dataIndex])
                                    dataIndex++
                                } else {
                                    filledDataList.add(currentHour to 0f)
                                }
                                currentHour++
                            }
                            filledDataList
                        }

                        phDataListWeekMonth.addAll(filledDataLists[0].map {date to it.second }.sortedBy { it.first })
                        tdsDataListWeekMonth.addAll(filledDataLists[1].map {date to it.second }.sortedBy { it.first })
                        waterlevelDataListWeekMonth.addAll(filledDataLists[2].map {date to it.second }.sortedBy { it.first })
                        watertempDataListWeekMonth.addAll(filledDataLists[3].map {date to it.second }.sortedBy { it.first })
                        humidityDataListWeekMonth.addAll(filledDataLists[4].map {date to it.second }.sortedBy { it.first })
                        airtempDataListWeekMonth.addAll(filledDataLists[5].map {date to it.second }.sortedBy { it.first })


                        getmaxValuesForY()
                        weeklyfunChart()
                        //progressbar.visibility = View.GONE
                    } else {
                        progressbar.visibility = View.GONE
                        println("Error getting documents: ${task.exception}")
                    }
                })
        }

    }

//    fun populateDaysOfTheWeek() {
//        val calendar = Calendar.getInstance()
//        val currentMonth = calendar.get(Calendar.MONTH) + 1
//        val currentYear = calendar.get(Calendar.YEAR)
//        daysOfTheWeek.clear()
//        datesOfTheWeek.clear()
//
//        calendar.set(Calendar.DAY_OF_MONTH, 1)
//
//        while (calendar.get(Calendar.MONTH) == currentMonth - 1 && calendar.get(Calendar.WEEK_OF_MONTH) < selectedWeek) {
//            calendar.add(Calendar.DAY_OF_MONTH, 1)
//        }
//
//        while (calendar.get(Calendar.MONTH) == currentMonth - 1 && calendar.get(Calendar.WEEK_OF_MONTH) == selectedWeek) {
//            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
//            daysOfTheWeek.add(dayOfMonth)
//            val monthString = (currentMonth).toString().padStart(2, '0')
//            val dayString = dayOfMonth.toString().padStart(2, '0')
//            datesOfTheWeek.add("$currentYear-$monthString-$dayString")
//            calendar.add(Calendar.DAY_OF_MONTH, 1)
//        }
//    }

    fun populateDaysOfTheWeek() {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        daysOfTheWeek.clear()
        datesOfTheWeek.clear()

        // Set the calendar to the first day of the selected month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, selectedMonth - 1) // Months are 0-indexed
        calendar.set(Calendar.YEAR, currentYear)

        // Move to the selected week
        while (calendar.get(Calendar.WEEK_OF_MONTH) < selectedWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Collect the days of the selected week
        while (calendar.get(Calendar.MONTH) == selectedMonth - 1 && calendar.get(Calendar.WEEK_OF_MONTH) == selectedWeek) {
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            daysOfTheWeek.add(dayOfMonth)
            val monthString = (selectedMonth).toString().padStart(2, '0')
            val dayString = dayOfMonth.toString().padStart(2, '0')
            datesOfTheWeek.add("$currentYear-$monthString-$dayString")
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }


    private fun populateDaysOfTheMonth() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        daysOfTheWeek.clear()
        datesOfTheWeek.clear()

        calendar.set(Calendar.YEAR, currentYear)
        calendar.set(Calendar.MONTH, selectedMonth - 1) // Adjust to the selected month
        calendar.set(Calendar.DAY_OF_MONTH, 1) // Set to the first day of the selected month

        // Iterate through all days of the selected month
        while (calendar.get(Calendar.MONTH) == selectedMonth - 1) {
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            daysOfTheWeek.add(dayOfMonth)
            val monthString = selectedMonth.toString().padStart(2, '0')
            val dayString = dayOfMonth.toString().padStart(2, '0')
            datesOfTheWeek.add("$currentYear-$monthString-$dayString")
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to the next day
        }
        //TestText.text = datesOfTheWeek.toString()
        //toast((selectedMonth - 1).toString())
    }
    private fun spinnerweekfun() {
        var week = arrayOf(0,1,2,3,4, 5)
        val spinneritems = arrayOf("none", "Week 1", "Week 2", "Week 3", "Week 4", "Week 5")
        val arraySpinner = ArrayAdapter(this@DataReports, android.R.layout.simple_spinner_dropdown_item, spinneritems)
        spinnerWeek.adapter = arraySpinner
        spinnerWeek?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //toast("item selected" + spinneritems[position])
                selectedWeek = week[position]
                weeklyDataLists.clear()
                //toast(selectedWeek.toString())

                phDataListWeekMonth.clear()
                tdsDataListWeekMonth.clear()
                waterlevelDataListWeekMonth.clear()
                watertempDataListWeekMonth.clear()
                humidityDataListWeekMonth.clear()
                airtempDataListWeekMonth.clear()

                showButtonWeekMonth.visibility = View.VISIBLE
                showButton.visibility = View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spinnerMonth.setSelection(selectedMonth)
        spinnerWeek.setSelection(selectedWeek)
    }

    private fun spinnermonthfun() {
        var month = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11,12)
        val spinneritems = arrayOf("none", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val arraySpinner = ArrayAdapter(this@DataReports, android.R.layout.simple_spinner_dropdown_item, spinneritems)
        spinnerMonth.adapter = arraySpinner
        spinnerMonth?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //toast("item selected" + spinneritems[position])
                weeklyDataLists.clear()
                selectedMonth = month[position]
                //toast(selectedMonth.toString())

                phDataListWeekMonth.clear()
                tdsDataListWeekMonth.clear()
                waterlevelDataListWeekMonth.clear()
                watertempDataListWeekMonth.clear()
                humidityDataListWeekMonth.clear()
                airtempDataListWeekMonth.clear()

                showButtonWeekMonth.visibility = View.VISIBLE
                showButton.visibility = View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spinnerMonth.setSelection(selectedMonth)
    }

    interface DataRetrievalCallback {
        fun onDataRetrieved()
    }

    private fun getDataFromFirebase(callback: DataRetrievalCallback) {
        progressbar.visibility = View.VISIBLE
        val db = FirebaseUtils.firestore
        val userReportsRef = db.collection("user").document(userID).collection("report")
        val query = userReportsRef.whereEqualTo("date", DateToday.toString())// query to filter documents based on DateToday
        query.get()
            .addOnSuccessListener { documents ->

                val temporaryDataLists = mutableListOf(
                    mutableListOf<Pair<Int, Float>>(), // pH
                    mutableListOf<Pair<Int, Float>>(), // TDS
                    mutableListOf<Pair<Int, Float>>(), // Water level
                    mutableListOf<Pair<Int, Float>>(), // Water temp
                    mutableListOf<Pair<Int, Float>>(), // Humidity
                    mutableListOf<Pair<Int, Float>>()) // Air temp

                for (document in documents) {// Loop through each document
                    val ph = document.getDouble("ph")?.toFloat()
                    val tds = document.getDouble("tds")?.toFloat()
                    val waterlevel = document.getDouble("waterlevel")?.toFloat()
                    val watertemp = document.getDouble("watertemp")?.toFloat()
                    val humidity = document.getDouble("humidity")?.toFloat()
                    val airtemp = document.getDouble("temperature")?.toFloat()
                    val hour = document.getDouble("hour")?.toInt()

                    val data = listOf(hour to ph, hour to tds, hour to waterlevel, hour to watertemp, hour to humidity, hour to airtemp)

                    data.forEachIndexed { index, pair ->
                        if (hour != null && pair.second != null) {
                            temporaryDataLists[index].add(hour to pair.second!!)
                        }
                    }
                }

                temporaryDataLists.forEach { dataList ->
                    dataList.sortBy { it.first }
                }

                // Fill the gaps of missing time
                val filledDataLists = temporaryDataLists.map { dataList ->
                    val filledDataList = mutableListOf<Pair<Int, Float>>()
                    var currentHour = 7
                    var dataIndex = 0

                    while (currentHour <= 18) {
                        if (dataIndex < dataList.size && dataList[dataIndex].first == currentHour) {
                            filledDataList.add(dataList[dataIndex])
                            dataIndex++
                        } else {
                            filledDataList.add(currentHour to 0f)
                        }
                        currentHour++
                    }
                    filledDataList
                }
                // Now filledDataLists contains sorted and gap-filled data lists for each parameter
                phDataList = filledDataLists[0].map { it.second }.toMutableList()
                tdsDataList = filledDataLists[1].map { it.second }.toMutableList()
                waterlevelDataList = filledDataLists[2].map { it.second }.toMutableList()
                watertempDataList = filledDataLists[3].map { it.second }.toMutableList()
                humidityDataList = filledDataLists[4].map { it.second }.toMutableList()
                airtempDataList = filledDataLists[5].map { it.second }.toMutableList()

                getmaxValuesForY()

                callback.onDataRetrieved()
                //progressbar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                progressbar.visibility = View.GONE
                // Handle any errors
                Log.e("Firestore", "Error getting documents: ", exception)
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
                if (radioDaily.isChecked) {
                    setLabelCount(12, false) // Set the label count to 12
                    valueFormatter = IndexAxisValueFormatter(getLabels().map { it.toString() }.toTypedArray()) // Convert Int to String
                } else {
                    setLabelCount(0, true) // Disable the labels
                    valueFormatter = null // Clear the value formatter
                }
            }
            animateY(1000)
            invalidate()
        }
    }


    private fun getLabels(): Array<Int> {
        return arrayOf(7,8,9,10,11,12,13,14,15,16,17,18,19)
    }
    private fun getmaxValuesForY() {
        if (radioDaily.isChecked){
            phmax = phDataList.maxOrNull() ?: 0.0f
            tdsmax = tdsDataList.maxOrNull() ?: 0.0f
            waterlevelmax = waterlevelDataList.maxOrNull() ?: 0.0f
            watertempmax = watertempDataList.maxOrNull() ?: 0.0f
            humiditymax = humidityDataList.maxOrNull() ?: 0.0f
            airtempmax = airtempDataList.maxOrNull() ?: 0.0f
        }else{
            phmax = 20f
            tdsmax = 1000f
            waterlevelmax = 20f
            watertempmax = 40f
            humiditymax = 100f
            airtempmax = 40f
        }
    }



    private fun weeklyfunChart() {
        progressbar.visibility = View.VISIBLE
        updateBarChartWithData(phBarChart, phDataListWeekMonth.map { it.second }, 1f, phmax + 5f, "ph Data", true)
        updateBarChartWithData(tdsBarChart, tdsDataListWeekMonth.map { it.second }, 1f, tdsmax + 10f, "tds Data", true)
        updateBarChartWithData(wlevelBarChart, waterlevelDataListWeekMonth.map { it.second }, 1f, waterlevelmax + 10f, "water level Data", true)
        updateBarChartWithData(wtempBarChart, watertempDataListWeekMonth.map { it.second }, 1f, watertempmax + 20f, "water temp Data", true)
        updateBarChartWithData(humidityBarChart, humidityDataListWeekMonth.map { it.second }, 1f, humiditymax + 20f, "humidity Data", true)
        updateBarChartWithData(airtempBarChart, airtempDataListWeekMonth.map { it.second }, 1f, airtempmax + 20f, "air temp Data", true)
    }

    private fun dailyfunChart() {
        progressbar.visibility = View.VISIBLE
        updateBarChartWithData(phBarChart, phDataList, 1f, phmax + 5f, "ph Data", false)
        updateBarChartWithData(tdsBarChart, tdsDataList, 1f, tdsmax + 10f, "tds Data", false)
        updateBarChartWithData(wlevelBarChart, waterlevelDataList, 1f, waterlevelmax + 10f, "water level Data", false)
        updateBarChartWithData(wtempBarChart, watertempDataList, 1f, watertempmax + 20f, "water temp Data", false)
        updateBarChartWithData(humidityBarChart, humidityDataList, 1f, humiditymax + 20f, "humidity Data", false)
        updateBarChartWithData(airtempBarChart, airtempDataList, 1f, airtempmax + 20f, "air temp Data", false)
    }
    private fun showButtonfun() {
        getDataFromFirebase(object : DataRetrievalCallback {
            override fun onDataRetrieved() {
                dailyfunChart()
            }
        })
        showDateTextView.text = "Date: " + DateToday.toString()
        showButton.visibility = View.GONE
        setDateButton.visibility = View.VISIBLE
    }

    private fun setDatefun() {
        val datePickerDialog = DatePickerDialog(this,{DatePicker, year: Int, monthOfYear: Int, dayofMonth: Int ->
            val selectDate: Calendar = Calendar.getInstance()
            selectDate.set(year, monthOfYear, dayofMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate: String = dateFormat.format(selectDate.time)
            showDateTextView.text = "Date: " + formattedDate
            DateToday = formattedDate.toLocalDate()
            showButton.visibility = View.VISIBLE
            setDateButton.visibility = View.GONE
            showButtonWeekMonth.visibility = View.GONE
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun getDateToday() {
        val currentDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        DateToday = currentDate
        showDateTextView.text = "Date: " + DateToday.toString()

        val calendar = Calendar.getInstance()
        val currentWeekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
        val currentMonth = Calendar.MONTH
        val currentYear = calendar.get(Calendar.YEAR)
        selectedMonth = currentMonth
        selectedWeek = currentWeekOfMonth
        selectedYear = currentYear
        //toast(selectedYear.toString())
    }


}
