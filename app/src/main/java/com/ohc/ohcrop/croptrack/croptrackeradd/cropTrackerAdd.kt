package com.ohc.ohcrop.croptrack.croptrackeradd

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.ohc.ohcrop.CropTracker
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.croptrack.croptrackerview.cropTrackerView
import com.ohc.ohcrop.databinding.ActivityCropTrackerAddBinding
import com.ohc.ohcrop.databinding.ActivityMonitorBinding
import com.ohc.ohcrop.reports.ChartChoice
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random


class cropTrackerAdd : AppCompatActivity() {
    private lateinit var binding: ActivityCropTrackerAddBinding
    private lateinit var userID: String

    private var referenceId: Int = 0;
    private var details: Boolean = false
    private var sowing: Boolean = false
    private var transplant: Boolean = false

    private lateinit var typeofPlant: String
    private var sowingDuration: Int = 0
    private var transplantDuration: Int = 0

    private val calendar = Calendar.getInstance()

    private var sowingDate: String = ""
    private var sowingendDate: String = ""

    private var transplantDate: String = ""
    private var transplantendDate: String = ""
    private var newDate: String = ""

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_tracker_add)

        binding = ActivityCropTrackerAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid

        binding.imageBtnBack.setOnClickListener {
            startActivity(Intent(this, CropTracker::class.java))
            finish()
        }
        binding.imageBtnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }
        //----------------------------------------------------------
        binding.saveButton.setOnClickListener {
            checkInputs()
        }

        binding.sowingDatebtn.setOnClickListener {
            setSowingDatefun()
        }
        binding.transplantDatebtn.setOnClickListener {
            setTransplantDatefun()
        }

        binding.linearSowingEndDate.visibility = View.GONE
        binding.linearTransplantEndDate.visibility = View.GONE
        binding.sowingSpinner.visibility = View.INVISIBLE
        binding.transplantSpinner.visibility = View.INVISIBLE

        typeofplant()
        sowingSpinner()
        transplantSpinner()

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@cropTrackerAdd, CropTracker::class.java)
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

    private fun setTransplantDatefun() {
        val datePickerDialog = DatePickerDialog(
            this,
            { datePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate: Calendar = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate: String = dateFormat.format(selectedDate.time)

                transplantDate = formattedDate.toString()

                val editableDate = SpannableStringBuilder(transplantDate.toString())
                binding.transplantText.text = editableDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun setSowingDatefun() {
        val datePickerDialog = DatePickerDialog(
            this,
            { datePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate: Calendar = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate: String = dateFormat.format(selectedDate.time)

                sowingDate = formattedDate

                val editableDate = SpannableStringBuilder(sowingDate.toString())
                binding.sowingText.text = editableDate
                binding.sowingSpinner.visibility = View.VISIBLE
                binding.transplantSpinner.visibility = View.VISIBLE
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()

    }

    private fun checkInputs() {
        if (!details) {
            if (binding.nodNameText.text.isNullOrEmpty() || binding.noofCropText.text.isNullOrEmpty() || typeofPlant.isNullOrEmpty()) {
                toast("Fill in your details")
                return
            } else {
                details = true
            }
        }

        if (!sowing) {
            if (binding.sowingText.text.isNullOrEmpty() || sowingDuration == 0) {
                toast("Fill in your sowing details")
                return
            } else {
                sowing = true
            }
        }

        if (!transplant) {
            if (binding.transplantText.text.isNullOrEmpty() || transplantDuration == 0) {
                toast("Fill in your transplant details")
                return
            } else {
                transplant = true
            }
        }
        if (details && sowing && transplant) {
            referenceId = Random.nextInt(1000000)
            saveFirestoreData()
        }
    }


    private fun saveFirestoreData() {
        binding.progressBar.visibility = View.VISIBLE
        val db = FirebaseUtils.firestore
        val data = hashMapOf(
            "referenceID" to "CT"+referenceId.toString(),
            "nodeName" to binding.nodNameText.text.toString(),
            "noOfCrops" to binding.noofCropText.text.toString().toInt(),
            "typeofplant" to typeofPlant,
            "sowingDate" to binding.sowingText.text.toString(),
            "sowingDuration" to sowingDuration,
            "sowingEndDate" to binding.endSowingText.text.toString(),
            "transplantDate" to binding.transplantText.text.toString(),
            "transplantDuration" to transplantDuration,
            "transplantEndDate" to binding.endTransplantText.text.toString(),
            // Add more fields as needed
        )
        val docRef = db.collection("user").document(userID).collection("croptrack").document()
        docRef.set(data)
            .addOnSuccessListener {
                toast("Saved successfully")
                val intent = Intent(this, cropTrackerView::class.java)
                intent.putExtra("referenceID","CT$referenceId")
                this.startActivity(intent)
                finish()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                // Error occurred while saving data
                toast("Error: ${e.message}")
            }
    }

    private fun typeofplant() {
        val spinneritems = arrayOf("none", "Lettuce", "Pipino", "Tomato", "Eggplant", "Bell Pepper", "Okra", "Basil")
        val arraySpinner = ArrayAdapter(this@cropTrackerAdd, android.R.layout.simple_spinner_dropdown_item, spinneritems)
        binding.typePlantSpinner.adapter = arraySpinner
        binding.typePlantSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                toast("item selected" + spinneritems[position])
                typeofPlant = spinneritems[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private fun sowingSpinner() {
        val minRange = 0
        val maxRange = 30
        val spinnerItems = (minRange..maxRange).map { it.toString() }.toTypedArray()

        val arraySpinner = ArrayAdapter(this@cropTrackerAdd, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        binding.sowingSpinner.adapter = arraySpinner
        binding.sowingSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                toast("item selected" + spinnerItems[position])
                sowingDuration = position
                setEndDateSowing(sowingDate, sowingDuration)
                binding.transplantText.setText(newDate)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun transplantSpinner() {
        val minRange = 0
        val maxRange = 60
        val spinnerItems = (minRange..maxRange).map { it.toString() }.toTypedArray()

        val arraySpinner = ArrayAdapter(this@cropTrackerAdd, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        binding.transplantSpinner.adapter = arraySpinner
        binding.transplantSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                toast("Item selected: ${spinnerItems[position]}")
                transplantDuration = position

                transplantDate = binding.transplantText.text.toString()
                setEndDateTransplant(binding.transplantText.text.toString(), transplantDuration)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    private fun setEndDateSowing(setDateString: String,  duration: Int) {
        if(setDateString != ""){
            val DateString = setDateString
            val parts = DateString.split("-")

            var year = parts[0].toInt()
            var month = parts[1].toInt()
            var day = parts[2].toInt()

            day += duration // add days

            while (day > daysInMonth(year, month)) {
                day -= daysInMonth(year, month)
                month++
                if (month > 12) {
                    month = 1
                    year++
                }
            }
            newDate = "$year-$month-$day"

            sowingendDate = newDate
            binding.endSowingText.setText(sowingendDate)
            binding.linearSowingEndDate.visibility = View.VISIBLE
        }
    }

    private fun setEndDateTransplant(setDateString: String,  duration: Int) {
        if(setDateString != ""){
            val DateString = setDateString
            val parts = DateString.split("-")

            var year = parts[0].toInt()
            var month = parts[1].toInt()
            var day = parts[2].toInt()

            day += duration // add days

            while (day > daysInMonth(year, month)) {
                day -= daysInMonth(year, month)
                month++
                if (month > 12) {
                    month = 1
                    year++
                }
            }
            newDate = "$year-$month-$day"

            transplantendDate = newDate
            binding.endTransplantText.setText(transplantendDate)
            binding.linearTransplantEndDate.visibility = View.VISIBLE
        }
    }
    private fun daysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // month is 0-based in Calendar
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}