package com.ohc.ohcrop.howto.appnavigation

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
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.HowTo
import com.ohc.ohcrop.R
import com.ohc.ohcrop.databinding.ActivityAppNavigationBinding
import com.ohc.ohcrop.databinding.ActivityMonitoringSensorsBinding

class AppNavigation : AppCompatActivity() {

    private lateinit var binding: ActivityAppNavigationBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_navigation)

        binding = ActivityAppNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener{
            showcustomDialog(1)
        }
        binding.register.setOnClickListener{
            showcustomDialog(2)
        }
        binding.dashboard.setOnClickListener{
            showcustomDialog(3)
        }
        binding.monitor.setOnClickListener{
            showcustomDialog(4)
        }
        binding.control.setOnClickListener{
            showcustomDialog(5)
        }
        binding.croptack.setOnClickListener{
            showcustomDialog(6)
        }
        binding.report.setOnClickListener{
            showcustomDialog(7)
        }
        binding.live.setOnClickListener{
            showcustomDialog(8)
        }
        binding.knowhow.setOnClickListener{
            showcustomDialog(9)
        }
        binding.profile.setOnClickListener{
            showcustomDialog(10)
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@AppNavigation, HowTo::class.java)
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

    private fun showcustomDialog(position: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(1)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.howto_appnavigation_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image: ImageView = dialog.findViewById(R.id.howTo_card_image)
        val button: Button = dialog.findViewById(R.id.howTo_card_button)

        when(position){
            1 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_login))
            }
            2 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_signup))
            }
            3 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_dashboard))
            }
            4 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_monitor))
            }
            5 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_control))
            }
            6 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_croptrack))
            }
            7 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_reports))
            }
            8 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_live))
            }
            9 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_howto))
            }
            10 -> {
                image.setImageDrawable(getDrawable(R.drawable.app_navigation_login))
            }

        }

        button.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()

    }
}