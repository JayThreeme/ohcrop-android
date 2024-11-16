package com.ohc.ohcrop.calculator.nutrientsolution

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.ohc.ohcrop.Calculator
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import org.w3c.dom.Text

class NutrientSolutionMixing : AppCompatActivity() {

    private lateinit var ProfileImgButton : ImageButton
    private lateinit var backButton : ImageButton

    private lateinit var solA_text : TextView
    private lateinit var solB_text : TextView
    private lateinit var water_text : TextView

    private lateinit var ntresultsolA_text : TextView
    private lateinit var ntresultsolB_text : TextView
    private lateinit var ntresultwater_text : TextView

    private lateinit var resultcard : LinearLayout
    private lateinit var nutrientsolcard : LinearLayout
    private lateinit var watertankcard : LinearLayout
    private lateinit var calculate_reset_btn : Button

    private lateinit var calculate_nt_btn : Button

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrient_solution_mixing)

        val enterAnimation = R.anim.slide_in_left
        val exitAnimation = R.anim.slide_out_right
        val options = ActivityOptions.makeCustomAnimation(this, enterAnimation, exitAnimation)

        ProfileImgButton = findViewById(R.id.imageBtnProfile)
        backButton = findViewById(R.id.imageBtnBack)

        solA_text = findViewById(R.id.solutionAText)
        solB_text = findViewById(R.id.solutionBText)
        water_text = findViewById(R.id.watertankvol_text)

        ntresultsolA_text = findViewById(R.id.nt_result_solA)
        ntresultsolB_text = findViewById(R.id.nt_result_solB)
        ntresultwater_text = findViewById(R.id.nt_result_water)

        calculate_nt_btn = findViewById(R.id.calc_nutrientsoln_btn)

        resultcard = findViewById(R.id.result_card)
        nutrientsolcard = findViewById(R.id.nutrientsolution_card)
        watertankcard = findViewById(R.id.watertank_card)
        calculate_reset_btn = findViewById(R.id.calc_reset_btn)

        calculate_reset_btn.setOnClickListener {
            resetfun()
        }


        ProfileImgButton.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, Calculator::class.java), options.toBundle())
            finish()
        }


        solA_text.setOnClickListener {
            solA_text.text = ""
        }

        solB_text.setOnClickListener {
            solB_text.text = ""
        }

        water_text.setOnClickListener {
            water_text.text = ""
        }

        calculate_nt_btn.setOnClickListener {
            calculate_nutrient()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@NutrientSolutionMixing, Calculator::class.java)
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

    private fun resetfun() {
        resultcard.visibility = View.GONE
        nutrientsolcard.visibility = View.VISIBLE
        watertankcard.visibility = View.VISIBLE
        calculate_reset_btn.visibility = View.GONE
        calculate_nt_btn.visibility = View.VISIBLE
        solA_text.text = "0"
        solB_text.text = "0"
        water_text.text = "0"
    }

    private fun calculate_nutrient() {
        val solA = solA_text.text.toString().toInt()
        val solB = solB_text.text.toString().toInt()
        val watervol = water_text.text.toString().toInt()

        if ((solA != 0) || (solB != 0) || (watervol != 0)){
           val solA_result = watervol * solA
            val solB_result = watervol * solB

            ntresultsolA_text.text = solA_result.toString() + " ml"
            ntresultsolB_text.text = solB_result.toString() + " ml"
            ntresultwater_text.text = watervol.toString() + " Liters"
            resultcard.visibility = View.VISIBLE
            nutrientsolcard.visibility = View.GONE
            watertankcard.visibility = View.GONE
            calculate_reset_btn.visibility = View.VISIBLE
            calculate_nt_btn.visibility = View.GONE
        }
    }
}