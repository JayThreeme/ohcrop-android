package com.ohc.ohcrop.howto.hydroponiccrops

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
import com.ohc.ohcrop.HowTo
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.databinding.ActivityAppNavigationBinding
import com.ohc.ohcrop.databinding.ActivityHydroponicCropsBinding
import com.ohc.ohcrop.databinding.ActivityMonitoringAndControlBinding

class HydroponicCrops : AppCompatActivity() {

    private lateinit var binding: ActivityHydroponicCropsBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hydroponic_crops)

        binding = ActivityHydroponicCropsBinding.inflate(layoutInflater)
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
                val intent = Intent(this@HydroponicCrops, HowTo::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


        binding.button1.setOnClickListener{
            showCustomDialog(1)
        }
        binding.button2.setOnClickListener{
            showCustomDialog(2)
        }
        binding.button3.setOnClickListener{
            showCustomDialog(3)
        }
        binding.button4.setOnClickListener{
            showCustomDialog(4)
        }
        binding.button5.setOnClickListener{
            showCustomDialog(5)
        }
    }

    private fun showCustomDialog(position: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(1)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.hydroponiccrops_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image: ImageView = dialog.findViewById(R.id.image)
        val title: TextView = dialog.findViewById(R.id.texttitle)
        val detail: TextView = dialog.findViewById(R.id.textdescription)
        val button: Button = dialog.findViewById(R.id.close)

        when(position){
            1 -> {
                image.setImageDrawable(getDrawable(R.drawable.crops_lettuce))
                title.text = "Lettuce"
                detail.text = "\t\tLettuce is a leafy green vegetable known for its crisp texture and mild flavor. It belongs to the Asteraceae family and is widely cultivated worldwide. Lettuce comes in various types, including iceberg, romaine, butterhead, and leaf lettuce, each with distinct shapes, colors, and flavors. It is commonly used in salads, sandwiches, wraps, and as a garnish. Lettuce is rich in vitamins A and K, as well as folate and fiber, making it a nutritious addition to any diet."
            }
            2 -> {
                image.setImageDrawable(getDrawable(R.drawable.crops_cucumber))
                title.text = "Cucumber"
                detail.text = "\t\tCucumber is a versatile fruit, often consumed as a vegetable, known for its refreshing taste and crunchy texture. Belonging to the gourd family, cucumbers are cylindrical in shape, with a smooth, green skin that may be ridged or smooth depending on the variety. They are primarily composed of water, making them hydrating and low in calories. Cucumbers can be eaten raw in salads, sandwiches, or as a snack, and they are also pickled to create various dishes and condiments. Rich in vitamins, minerals, and antioxidants, cucumbers offer numerous health benefits, including improved hydration and digestion."
            }
            3 -> {
                image.setImageDrawable(getDrawable(R.drawable.crops_tomato))
                title.text = "Tomato"
                detail.text = "\t\tTomato is a popular fruit widely used as a vegetable in cooking. It comes in various shapes, sizes, and colors, including red, yellow, and green. Tomatoes are known for their juicy texture and sweet-tart flavor, which can vary depending on the variety. They are rich in vitamins C and K, as well as antioxidants like lycopene, which may have various health benefits. Tomatoes are used in a wide range of dishes, including salads, sauces, soups, sandwiches, and as a topping on pizzas and pastas. They can be consumed fresh, cooked, or preserved through canning or drying methods."
            }
            4 -> {
                image.setImageDrawable(getDrawable(R.drawable.crops_eggplant))
                title.text = "Eggplant"
                detail.text = "\t\tEggplant, also known as aubergine, is a versatile vegetable belonging to the nightshade family. It is characterized by its deep purple skin and elongated shape, though there are variations in color and size. Eggplant has a mild, slightly bitter flavor and a spongy texture when cooked. Commonly used in Mediterranean, Middle Eastern, and Asian cuisines, eggplant can be grilled, roasted, sautéed, or fried. It is often used in dishes such as ratatouille, moussaka, baba ganoush, and eggplant parmesan. Eggplant is low in calories and rich in fiber, vitamins, and minerals, making it a nutritious addition to various dishes."
            }
            5 -> {
                image.setImageDrawable(getDrawable(R.drawable.crops_strawberry))
                title.text = "Strawberry"
                detail.text = "\t\tStrawberry is a vibrant and juicy fruit renowned for its sweet flavor and bright red color. It belongs to the rose family and is characterized by its small, conical shape and numerous tiny seeds embedded on its surface. Strawberries are enjoyed fresh as a snack, in desserts such as cakes, pies, and ice creams, and as a topping for cereals and salads. They are also used to make jams, preserves, and sauces. Rich in vitamin C, fiber, and antioxidants, strawberries offer numerous health benefits and are a popular fruit worldwide, especially during the summer season."
            }
        }

        button.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove callback when activity is destroyed
        onBackPressedCallback.remove()
    }
}