package com.ohc.ohcrop.reports

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.ohc.ohcrop.R
import com.ohc.ohcrop.utils.FirebaseUtils
import io.data2viz.charts.chart.Chart
import io.data2viz.charts.chart.chart
import io.data2viz.geom.Size
import io.data2viz.charts.chart.discrete
import io.data2viz.viz.VizContainerView
import io.data2viz.charts.chart.mark.area
import io.data2viz.charts.chart.quantitative
import java.util.Calendar
import java.util.Date
import com.ohc.ohcrop.utils.Extensions
import com.ohc.ohcrop.utils.Extensions.toast
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Locale

class BarChart : AppCompatActivity() {
    private lateinit var actionBar: ActionBar
    private lateinit var phBarChart: BarChart
    private lateinit var progressbar: ProgressBar
    private lateinit var setDateButton: Button
    private lateinit var showButton: Button
    private lateinit var showDateEditText: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioDaily: RadioButton
    private lateinit var radioWeekly: RadioButton
    private lateinit var radioMonthly: RadioButton


    private val dataValuePairs = ArrayList<DataPair>()
    private val  listData:ArrayList<BarEntry> = ArrayList()


    private lateinit var userID: String
    private lateinit var dataToday: LocalDate
    private lateinit var setDate: String
    private val calendar = Calendar.getInstance()
    private lateinit var passedreports: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)
        setSupportActionBar(findViewById(R.id.barchart_toolbar))

        getDateToday()//get local date today

        actionBar = supportActionBar!!
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid
        phBarChart = findViewById(R.id.barchart_chart)
        progressbar = findViewById(R.id.progress_bar)
        setDateButton = findViewById(R.id.select_button)
        showButton = findViewById(R.id.show_button)
        showDateEditText = findViewById(R.id.set_date_textview)
        radioGroup = findViewById(R.id.radiogroup)
        radioDaily = findViewById(R.id.radioDaily)
        radioWeekly = findViewById(R.id.radioWeekly)
        radioMonthly = findViewById(R.id.radioMonthly)

        showButton.isEnabled = false
        radioDaily.isChecked = true

        passedreports = intent.getStringExtra("reports").toString()
        actionbarfun("${passedreports.replaceFirstChar { c: Char -> c.uppercase() }}")


        firebasedatafun(passedreports, dataToday.toString())//call function

        setDateButton.setOnClickListener {
            setDatefun()
        }
        showButton.setOnClickListener {
            dataValuePairs.clear()
            listData.clear()
            firebasedatafun(passedreports, setDate)
            showButton.isEnabled = false
        }
    }
    private fun actionbarfun(passAstring: String) {
        if (actionBar != null) {
            actionBar.setSubtitle("${passAstring.uppercase()} (Daily)")
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun firebasedatafun(whatreport:String, whatDate:String) {
        val sevenDaysAgo = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -7)
        }.time

        val reportsData = FirebaseUtils.firestore
            .collection("user")
            .document(userID)
            .collection("report")
            .whereEqualTo("date", whatDate)
            reportsData.get()
            .addOnSuccessListener {documents ->
                for (document in documents){
                    //Log.d(TAG, "${document.id} => ${document.data}")
                    val datavalue: Any? = document[whatreport]
                    val dataday = document["hour"]
                    if(datavalue != null){
                        val datadays = dataday.toString().toDouble().toFloat()
                        val datavalues = datavalue.toString().toDouble().toFloat()
                        dataValuePairs.add(DataPair(datadays,datavalues))
                        //listData.add(BarEntry(1f, 10f))
                        Log.d(TAG, "${datadays}")
                        Log.d(TAG, "${datavalues}")
                    }else{
                        Log.d(TAG, "phValue is null or not present for document ${document.id}")
                    }
                    //listData.add(BarEntry(counter+1f, datavalues.toFloat()))
                    //Log.d(TAG, "${counter}")
                }
                Log.d(TAG, "${dataValuePairs}")
                //Log.d(TAG, "${listData}")
                processlists()
            }
            .addOnFailureListener {
                    e -> Log.w(ContentValues.TAG, "Error writing document",e)
            }
    }

    private fun processlists() {
        var floatcount: Float = 0f

//        listData.add(BarEntry(2f, 20f))

        for (dataPair in dataValuePairs) {
            //println("Key: ${dataPair.key}, Value: ${dataPair.value}")
            listData.add(BarEntry(dataPair.key+1f, dataPair.value))
        }

        val barDataSet = BarDataSet(listData, "Values")
        barDataSet.setColor(ColorTemplate.MATERIAL_COLORS[3], 255)
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)

        val legend = phBarChart.legend
        legend.setDrawInside(false)


        phBarChart.setFitBars(true)
        phBarChart.setDrawMarkers(false)
        phBarChart.setDrawGridBackground(false)

        phBarChart.data=barData
        phBarChart.description.text="Ph Chart"
        phBarChart.animateY(2000)
        phBarChart.invalidate()
    }

    private fun getDateToday() {//get local date
        val currentDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        // Store the current date in a variable (optional)
        val storedDate: String = currentDate.toString()
        dataToday = currentDate
    }

    private fun setDatefun() {//pick date show dialog
        val datePickerDialog = DatePickerDialog(this,{DatePicker, year: Int, monthOfYear: Int, dayofMonth: Int ->
            val selectDate: Calendar = Calendar.getInstance()
            selectDate.set(year, monthOfYear, dayofMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate: String = dateFormat.format(selectDate.time)
            setDate = formattedDate
            showDateEditText.text = formattedDate
            showButton.isEnabled = true
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}

data class DataPair(
    val key: Float,
    val value: Float
)



