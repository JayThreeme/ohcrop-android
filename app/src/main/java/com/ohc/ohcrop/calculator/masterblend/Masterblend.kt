package com.ohc.ohcrop.calculator.masterblend

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.ohc.ohcrop.Calculator
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.databinding.ActivityMasterblendBinding
import com.ohc.ohcrop.databinding.ActivityMonitorBinding
import com.ohc.ohcrop.utils.Extensions.toast

class Masterblend : AppCompatActivity() {
    private lateinit var binding: ActivityMasterblendBinding

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var selectedFormula : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masterblend)

        binding = ActivityMasterblendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val enterAnimation = R.anim.slide_in_left
        val exitAnimation = R.anim.slide_out_right
        val options = ActivityOptions.makeCustomAnimation(this, enterAnimation, exitAnimation)

        binding.imageBtnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

        binding.imageBtnBack.setOnClickListener {
            startActivity(Intent(this, Calculator::class.java), options.toBundle())
            finish()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@Masterblend, Calculator::class.java)
                startActivity(intent, options.toBundle())
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


        val waterlevels = arrayOf("none", "Tomato Formula 4-18-38", "Hydroponic Formula 5-11-26" ,"Lettuce Formula 8-15-36" ,"Strawberry Formula 9-12-34")
        val arraySpinner = ArrayAdapter(this@Masterblend, android.R.layout.simple_spinner_dropdown_item, waterlevels)
        binding.spinner.adapter = arraySpinner
        binding.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //toast("item selected" + waterlevels[position])
                selectedFormula = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.calculateButton.setOnClickListener {
            calculatefun()
        }

        binding.resultCard.visibility = View.GONE
        binding.calcResetBtn.visibility = View.GONE


        binding.calcResetBtn.setOnClickListener {
            resetfun()
        }
    }

    private fun resetfun() {
        binding.resultCard.visibility = View.GONE
        binding.calcResetBtn.visibility = View.GONE
        binding.calculateButton.visibility = View.VISIBLE
        binding.watertankCard.visibility = View.VISIBLE
        binding.measurementcard.visibility = View.VISIBLE
    }

    private fun calculatefun() {
        if (binding.watertankvolText.text.isEmpty() && selectedFormula == 0){
            toast("Enter Parameters")
        }else{
            when(selectedFormula){
                1 -> result(1.08, 0.89, 0.40)
                2 -> result(0.87, 1.30, 0.0)
                3 -> result(0.65, 0.60, 0.37)
                4 -> result(0.45, 0.45, 0.44)
            }
        }
    }

    private fun result(calNitdata: Double, masterblenddata : Double, epsomsaltdata: Double) {
        var literOfWater = binding.watertankvolText.text.toString().toInt()
        var calnit = literOfWater * calNitdata
        var masterblend = literOfWater * masterblenddata
        var epsomsalt = literOfWater * epsomsaltdata

        printfun(calnit, masterblend, epsomsalt)
    }

    private fun printfun(calnit: Double, masterblend: Double, epsomsalt: Double) {
        binding.result1.setText(calnit.toString())
        binding.result2.setText(masterblend.toString())
        binding.result3.setText(epsomsalt.toString())

        binding.resultCard.visibility = View.VISIBLE
        binding.calcResetBtn.visibility = View.VISIBLE
        binding.calculateButton.visibility = View.GONE
        binding.watertankCard.visibility = View.GONE
        binding.measurementcard.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove callback when activity is destroyed
        onBackPressedCallback.remove()
    }
}