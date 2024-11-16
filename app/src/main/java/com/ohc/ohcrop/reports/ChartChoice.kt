package com.ohc.ohcrop.reports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.R
import com.ohc.ohcrop.Reports
import com.ohc.ohcrop.reports.datareports.DataReports

// NOT IN USE
// NOT IN USE
// NOT IN USE
// NOT IN USE
class ChartChoice : AppCompatActivity() {
    private lateinit var backButton : ImageButton

    private lateinit var barcharbtn : Button
    private lateinit var linecharbtn : Button
    private lateinit var piecharbtn : Button
    private lateinit var scattercharbtn : Button
    private lateinit var radarcharbtn : Button

    private lateinit var titletextview : TextView

    private lateinit var passedreports: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart_choice)

        backButton = findViewById(R.id.imageBtnBack)

        barcharbtn = findViewById(R.id.barchartBtn)
        linecharbtn = findViewById(R.id.linechartbtn)
        piecharbtn = findViewById(R.id.piechartbtn)
        scattercharbtn = findViewById(R.id.scatterplotbtn)
        radarcharbtn = findViewById(R.id.radargraphbtn)

        titletextview = findViewById(R.id.choice_charttitle_textview)

        passedreports = intent.getStringExtra("reports").toString()
        titletextview.text = "${passedreports.replaceFirstChar { c: Char -> c.uppercase() }} Reports"


        barcharbtn.setOnClickListener {
            val intent = Intent(this, DataReports::class.java)
            //intent.putExtra("reports",passedreports)
            this.startActivity(intent)
        }

        linecharbtn.setOnClickListener {
//            val intent = Intent(this, BarChart::class.java)
//            intent.putExtra("reports","ph")
//            this.startActivity(intent)
        }

        piecharbtn.setOnClickListener {
//            val intent = Intent(this, BarChart::class.java)
//            intent.putExtra("reports","ph")
//            this.startActivity(intent)
        }

        scattercharbtn.setOnClickListener {
//            val intent = Intent(this, BarChart::class.java)
//            intent.putExtra("reports","ph")
//            this.startActivity(intent)
        }

        radarcharbtn.setOnClickListener {
//            val intent = Intent(this, BarChart::class.java)
//            intent.putExtra("reports","ph")
//            this.startActivity(intent)
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, Reports::class.java))
            finish()
        }


    }
}