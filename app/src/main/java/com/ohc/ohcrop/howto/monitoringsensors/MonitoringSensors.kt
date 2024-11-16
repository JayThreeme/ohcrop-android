package com.ohc.ohcrop.howto.monitoringsensors

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.media3.common.Timeline.Window
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.HowTo
import com.ohc.ohcrop.R
import com.ohc.ohcrop.databinding.ActivityMonitorBinding
import com.ohc.ohcrop.databinding.ActivityMonitoringSensorsBinding
import org.w3c.dom.Text

class MonitoringSensors : AppCompatActivity() {

    private lateinit var binding: ActivityMonitoringSensorsBinding

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring_sensors)

        binding = ActivityMonitoringSensorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnphsensor.setOnClickListener{
            //val title
            showCustomDialog(1)
        }
        binding.btntdssensor.setOnClickListener{
            showCustomDialog(2)
        }
        binding.btnwatersensor.setOnClickListener{
            showCustomDialog(3)
        }
        binding.btnwatertempsensor.setOnClickListener{
            showCustomDialog(4)
        }
        binding.btnhumiditysensor.setOnClickListener{
            showCustomDialog(5)
        }
        binding.btntempsensor.setOnClickListener{
            showCustomDialog(6)
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@MonitoringSensors, HowTo::class.java)
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

    private fun showCustomDialog(position: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(1)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.howto_custom_dialog_1)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image:ImageView = dialog.findViewById(R.id.howTo_card_image)
        val title:TextView = dialog.findViewById(R.id.howTo_card_title)
        val detail:TextView = dialog.findViewById(R.id.howTo_card_details)
        val button:Button = dialog.findViewById(R.id.howTo_card_button)

        when(position){
            1 -> {
                image.setImageDrawable(getDrawable(R.drawable.howto_phsensor))
                title.text = "PH Sensor"
                detail.text = "pH sensor is one of the most important tools for measuring pH and is commonly used " +
                        "in water quality monitoring. This type of sensor is capable of measuring alkalinity and " +
                        "acidity in water and other solutions. When used properly, pH sensors can ensure the safety " +
                        "and quality of products and processes that occur in wastewater or manufacturing plants."
            }
            2 -> {
                image.setImageDrawable(getDrawable(R.drawable.howto_tds))
                title.text = "TDS Sensor"
                detail.text = "TDS Meter Kit for measuring TDS value of the water, to reflect the cleanliness of the water." +
                        " TDS meter can be applied to domestic water, hydroponic and other fields of water quality testing."
            }
            3 -> {
                image.setImageDrawable(getDrawable(R.drawable.howto_ultrasonic))
                title.text = "Ultrasonic Distance Sensor"
                detail.text = "As the name indicates, ultrasonic sensors measure distance by using ultrasonic waves. The sensor head emits an ultrasonic wave and receives the wave reflected " +
                        "back from the target. Ultrasonic Sensors measure the distance to the target by measuring the time between the emission and reception."
            }
            4 -> {
                image.setImageDrawable(getDrawable(R.drawable.howto_watertemp))
                title.text = "Water Temperature Sensor"
                detail.text = "The core functionality of the DS18B20 is its direct-to- digital temperature sensor. " +
                        "The resolution of the tempera- ture sensor is user-configurable to 9, 10, 11, or 12 bits, corresponding " +
                        "to increments of 0.5°C, 0.25°C, 0.125°C, and 0.0625°C, respectively."
            }
            5 -> {
                image.setImageDrawable(getDrawable(R.drawable.howto_humtemp))
                title.text = "Humidity Sensor"
                detail.text = "The DHT11 is a commonly used Temperature and humidity sensor. The sensor comes with a dedicated NTC to measure temperature and " +
                        "an 8-bit microcontroller to output the values of " +
                        "temperature and humidity as serial data. The sensor is also factory calibrated and hence easy to interface with other microcontrollers."
            }
            6 -> {
                image.setImageDrawable(getDrawable(R.drawable.howto_humtemp))
                title.text = "Temperature Sensor"
                detail.text = "The DHT11 is a commonly used Temperature and humidity sensor. The sensor comes with a dedicated NTC to measure temperature and " +
                        "an 8-bit microcontroller to output the values " +
                        "of temperature and humidity as serial data. The sensor is also factory calibrated and hence easy to interface with other microcontrollers."
            }
        }

        button.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }
}