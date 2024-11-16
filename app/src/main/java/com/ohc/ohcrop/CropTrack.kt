package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.media3.common.util.Assertions
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates

class CropTrack : AppCompatActivity() {

    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var dateTodayText: TextView

    private lateinit var noOfCropText: TextView
    private lateinit var noOfCropPlusButton: Button
    private lateinit var noOfCropMinusButton: Button
    private lateinit var noOfCropSetButton: Button

    private lateinit var SowingStartDateText: TextView
    private lateinit var SowingEndDateText: TextView
    private lateinit var SowingDaysCount: TextView
    private lateinit var SowingStartDateButton: Button
    private lateinit var SowingEndDateButton: Button
    private lateinit var SowingSetButton: Button

    private lateinit var TranspltStartDateText: TextView
    private lateinit var TranspltEndDateText: TextView
    private lateinit var TranspltDaysCount: TextView
    private lateinit var TranspltStartDateButton: Button
    private lateinit var TranspltEndDateButton: Button
    private lateinit var TranspltSetButton: Button

    private lateinit var daystext1: TextView
    private lateinit var daystext2: TextView

    //variables
    private  val calendar = Calendar.getInstance()
    private lateinit var userID: String
    private lateinit var thedateToday: String
    private var dayToday by Delegates.notNull<Int>()
    private lateinit var noOfCrops: String
    private lateinit var SowingStartDate: String
    private lateinit var SowingEndDate: String
    private lateinit var TranplantStartDate: String
    private lateinit var TranplantEndDate: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_track)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid
        setCropTrackData()

        //header
        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)


        //set no of crops ids
        noOfCropText = findViewById(R.id.ctNoOfCropText)
        noOfCropPlusButton = findViewById(R.id.ctNoOfCropPLUS)
        noOfCropMinusButton = findViewById(R.id.ctNoOfCropMinus)
        noOfCropSetButton = findViewById(R.id.ctNoOfCropSet)

        //set date Today
        dateTodayText = findViewById(R.id.textDateToday)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dayToday = calendar.get(Calendar.DAY_OF_MONTH)
        thedateToday = ("$year-$month-$day").toString()
        dateTodayText.text = "Date Today: "+ thedateToday

        //find IDs
        SowingDaysCount = findViewById(R.id.ctSowingDaysCount)
        TranspltDaysCount = findViewById(R.id.ctTransplantDaysCount)

        SowingStartDateText = findViewById(R.id.ctSowingStartDateText)
        SowingEndDateText = findViewById(R.id.ctSowingEndDateText)
        SowingStartDateButton = findViewById(R.id.ctSowingStartDateButton)
        SowingEndDateButton = findViewById(R.id.ctSowingEndDateButton)
        SowingSetButton = findViewById(R.id.ctSowingSETBtn)

        TranspltStartDateText = findViewById(R.id.ctTranspltStartDateText)
        TranspltEndDateText = findViewById(R.id.ctTranspltEndDateText)
        TranspltStartDateButton = findViewById(R.id.ctTranspltStartDateButton)
        TranspltEndDateButton = findViewById(R.id.ctTranspltEndDateButton)
        TranspltSetButton = findViewById(R.id.ctTransplantSETBtn)

        daystext1 = findViewById(R.id.daystext1)
        daystext2 = findViewById(R.id.daystext2)

        noOfCropPlusButton.setOnClickListener {
            if(noOfCropText.text.toString().matches("-?\\d+(\\.\\d+)?".toRegex())){
                val numberofCropsParse = noOfCropText.text.toString()
                val counter = numberofCropsParse.toInt() + 1
                noOfCropText.text = counter.toString()
            }else{
                toast("wrong input")
            }

        }

        noOfCropMinusButton.setOnClickListener {
            if(noOfCropText.text.toString().matches("-?\\d+(\\.\\d+)?".toRegex())){
                val numberofCropsParse = noOfCropText.text.toString()
                val counter = numberofCropsParse.toInt() - 1
                noOfCropText.text = counter.toString()
            }else{
                toast("wrong input")
            }
        }

        noOfCropSetButton.setOnClickListener {
            SetNoOfCropData()
        }

        SowingSetButton.setOnClickListener {
            SowingData()
        }
        TranspltSetButton.setOnClickListener {
            TransplantData()
        }


        SowingStartDateButton.setOnClickListener {
            showDatePicker(1)
        }

        SowingEndDateButton.setOnClickListener {
            showDatePicker(2)
        }

        TranspltStartDateButton.setOnClickListener {
            showDatePicker(3)
        }

        TranspltEndDateButton.setOnClickListener {
            showDatePicker(4)
        }


        ProfileImgButton.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            toast("Profile")
            finish()
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            toast("Crop yield")
            finish()
        }


    }

    private fun SetNoOfCropData() {
        val noOfCrop = noOfCropText.text.toString()
        noOfCrops = noOfCrop
        FirebaseUtils.firestore.collection("user").document(userID).collection("croptrack").document(userID).update("noOfCrops", noOfCrop)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCropTrackData() {
        val ref = FirebaseUtils.firestore.collection("user").document(userID).collection("croptrack").document(userID)
        ref.get().addOnSuccessListener {
                if (it != null){
                    SowingStartDate = it.data?.get("sowingStartdate").toString()
                    SowingEndDate = it.data?.get("sowingEnddate").toString()
                    TranplantStartDate = it.data?.get("transStartdate").toString()
                    TranplantEndDate = it.data?.get("transEnddate").toString()
                    noOfCrops = it.data?.get("noOfCrops").toString()


                    SowingStartDateText.text = SowingStartDate
                    SowingEndDateText.text = SowingEndDate
                    TranspltStartDateText.text = TranplantStartDate
                    TranspltEndDateText.text = TranplantEndDate
                    noOfCropText.text = noOfCrops

                    SowingStartDateButton.text = "-"
                    SowingEndDateButton.text = "-"
                    TranspltStartDateButton.text = "-"
                    TranspltEndDateButton.text = "-"

                    croptrackCountDays()

                }else{
                    Log.d(ContentValues.TAG, "Failed Getting Sowing Data")
                }
            }
            .addOnFailureListener {
                    e -> Log.w(ContentValues.TAG, "Error writing document",e)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun SowingData() {
        if(notEmptySowing()){
            // call function and retrieve information on firestore

            croptrackCountDays()
            val sowingMap = hashMapOf(
                "sowingStartdate" to SowingStartDate,
                "sowingEnddate" to SowingEndDate,
            )

            FirebaseUtils.firestore.collection("user").document(userID).collection("croptrack").document(userID).update(
                sowingMap as Map<String, Any>
            )
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }

        }else{
            toast("Set Your Sowing Dates")
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun TransplantData() {
        if(notEmptyTransplant()){
            croptrackCountDays()

            val transmap = hashMapOf(
                "transStartdate" to TranplantStartDate,
                "transEnddate" to TranplantEndDate,
            )

            FirebaseUtils.firestore.collection("user").document(userID).collection("croptrack").document(userID).update(
                transmap as Map<String, Any>
            )
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document",e) }

        }else{
            toast("Set Your Transplant Dates")
        }

    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun croptrackCountDays() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-d")
              //val dayTodayis = LocalDate.parse(dateToday, DateTimeFormatter.ISO_DATE)
            val dayTodayis = LocalDate.parse(thedateToday, formatter)

                //val localDate = LocalDate.parse(dateString, formatter)

            val sowStartdateparse = LocalDate.parse(SowingStartDate, DateTimeFormatter.ISO_DATE)
            val sowStartDay = sowStartdateparse.dayOfMonth.toString().toInt()
            val sowEnddateparse = LocalDate.parse(SowingEndDate, DateTimeFormatter.ISO_DATE)
            val sownoOfDays = dayToday - sowStartDay

            if (sowEnddateparse < dayTodayis){
                SowingDaysCount.text = "Done"
                daystext1.text = ""
            }else{
                SowingDaysCount.text = sownoOfDays.toString()
            }

            val transStartdateparse = LocalDate.parse(TranplantStartDate, DateTimeFormatter.ISO_DATE)
            val transStartDay = transStartdateparse.dayOfMonth.toString().toInt()
            val transEnddateparse = LocalDate.parse(TranplantEndDate, DateTimeFormatter.ISO_DATE)
            val transnoOfDays = dayToday - transStartDay


            if (transEnddateparse < dayTodayis){
                TranspltDaysCount.text = "Done"
                daystext2.text = ""
            }else{
                TranspltDaysCount.text = transnoOfDays.toString()
            }
                //println("Parsed Date: $localDate")

    }

    private fun notEmptySowing(): Boolean = SowingStartDateText.text.toString().isNotBlank() && SowingEndDateText.text.toString().isNotBlank()
    private fun notEmptyTransplant(): Boolean = TranspltStartDateText.text.toString().isNotBlank() && TranspltEndDateText.text.toString().isNotBlank()
    private fun showDatePicker(setDate: Int) {
        val datePickerDialog = DatePickerDialog(this,{DatePicker, year: Int, monthOfYear: Int, dayofMonth: Int ->
            val selectDate: Calendar = Calendar.getInstance()
            selectDate.set(year, monthOfYear, dayofMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate: String = dateFormat.format(selectDate.time)

            when(setDate){
                1 -> {
                    SowingStartDateText.text = formattedDate
                    SowingStartDate = formattedDate
                }
                2 -> {
                    SowingEndDateText.text = formattedDate
                    SowingEndDate = formattedDate
                }
                3 -> {
                    TranspltStartDateText.text = formattedDate
                    TranplantStartDate = formattedDate
                }
                4 -> {
                    TranspltEndDateText.text = formattedDate
                    TranplantEndDate = formattedDate
                }
            }
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}