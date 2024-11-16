package com.ohc.ohcrop.howto.hydroponicsystems

import HydroponicSystemSlide
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.ohc.ohcrop.HowTo
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.databinding.ActivityHydroponicSystemsBinding
import com.ohc.ohcrop.databinding.ActivityMonitoringAndControlBinding
import hydroponicSystemAdapter

class HydroponicSystems : AppCompatActivity() {

    private lateinit var binding: ActivityHydroponicSystemsBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hydroponic_systems)

        binding = ActivityHydroponicSystemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageBtnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

        binding.imageBtnBack.setOnClickListener {
            startActivity(Intent(this, HowTo::class.java))
            finish()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@HydroponicSystems, HowTo::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        //HydroponicSystemSlide(R.drawable.hydroponic1, "", ""),
        val slides = listOf(
            HydroponicSystemSlide(R.drawable.hydroponic_deepwater, "Deep water culture systems", "\t\tDeep Water Culture (DWC) hydroponic systems suspend plant roots in a nutrient solution, promoting rapid growth. Plants are placed in containers filled with the solution, oxygenated by an air pump and air stone. Nutrient levels, pH, and temperature must be carefully monitored for optimal plant health. With proper maintenance, DWC systems offer efficient nutrient uptake and high yields.",R.drawable.hydroponic_deepwater_1,R.drawable.hydroponic_deepwater_2),
            HydroponicSystemSlide(R.drawable.hydroponic_wick, "Wick systems", "\t\tWick systems in hydroponics use a passive method where plants draw up nutrient solution through wicks from a reservoir below. No pumps are needed, making it simple and low-maintenance. While ideal for smaller-scale setups, it may be less efficient for larger operations due to limitations in nutrient distribution.",R.drawable.hydroponic_wick_1,R.drawable.hydroponic_wick_2),
            HydroponicSystemSlide(R.drawable.hydroponic_aeroponic, "Nutrient film technique systems", "\t\tNutrient Film Technique (NFT) hydroponic systems involve a continuous flow of nutrient solution over plant roots, held in channels with a slight slope. The roots are exposed to a thin film of nutrient solution, maximizing oxygen uptake. NFT systems are efficient, using minimal water and nutrients, making them suitable for various plants and scalable for commercial operations.",R.drawable.hydroponic_nft_1,R.drawable.hydroponic_nft_2),
            HydroponicSystemSlide(R.drawable.hydroponic_ebb, "Ebb and flow systems", "\t\tEbb and Flow, or Flood and Drain, hydroponic systems work by intermittently flooding the plant roots with nutrient solution before draining it away. This cyclic process provides plants with water, nutrients, and oxygen, promoting healthy growth. Ebb and flow systems are versatile, suitable for a wide range of plants, and can be easily automated for efficient operation.",R.drawable.hydroponic_ebb_1,R.drawable.hydroponic_ebb_2),
            HydroponicSystemSlide(R.drawable.hydroponic_drip, "Drip systems", "\t\tDrip hydroponic systems deliver nutrient solution directly to plant roots through a network of tubing and emitters. This method allows for precise control over water and nutrient delivery, promoting efficient nutrient uptake and plant growth. Drip systems are versatile, suitable for various plant types, and can be automated for ease of maintenance.",R.drawable.hydroponic_drip_1,R.drawable.hydroponic_drip_2),
            HydroponicSystemSlide(R.drawable.hydroponic_aeroponic, "Aeroponics", "\t\tAeroponics hydroponic systems mist plant roots with a nutrient solution, providing oxygen and nutrients directly to the root zone. This method promotes rapid plant growth and efficient nutrient absorption. With minimal water usage and no soil, aeroponics is a highly efficient and space-saving hydroponic technique often used in vertical farming and controlled environment agriculture.",R.drawable.hydroponic_aeroponic_1,R.drawable.hydroponic_aeroponic_2)
            )

        val viewPager: ViewPager2 = binding.viewPager
        val adapter = hydroponicSystemAdapter(slides)
        viewPager.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove callback when activity is destroyed
        onBackPressedCallback.remove()
    }
}