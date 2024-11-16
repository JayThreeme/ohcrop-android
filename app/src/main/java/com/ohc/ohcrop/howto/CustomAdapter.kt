package com.ohc.ohcrop.howto

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.control.fan.FanControl
import com.ohc.ohcrop.control.irrigation.IrrigationControl
import com.ohc.ohcrop.control.light.LightControl
import com.ohc.ohcrop.control.misting.MistingControl
import com.ohc.ohcrop.control.watertank.WaterTankControl
import com.ohc.ohcrop.howto.appnavigation.AppNavigation
import com.ohc.ohcrop.howto.hydroponiccrops.HydroponicCrops
import com.ohc.ohcrop.howto.hydroponicsystems.HydroponicSystems
import com.ohc.ohcrop.howto.monitorandcontrol.MonitoringAndControl
import com.ohc.ohcrop.howto.monitoringsensors.MonitoringSensors
import com.ohc.ohcrop.howto.whatishydroponic.Whatishydroponic

class CustomAdapter(private val mList: List<ItemsViewModel>, private val context: Context): RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.howto_card_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)
        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.text
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION){
                    when(position){
                        0 -> {
                            val intent = Intent(context, MonitoringAndControl::class.java)
                            intent.putExtra("selectedItem", position)
                            context.startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(context, MonitoringSensors::class.java)
                            intent.putExtra("selectedItem", position)
                            context.startActivity(intent)
                        }
                        2 -> {
                            val intent = Intent(context, AppNavigation::class.java)
                            intent.putExtra("selectedItem", position)
                            context.startActivity(intent)
                        }
                        3 -> {
                            val intent = Intent(context, Whatishydroponic::class.java)
                            intent.putExtra("selectedItem", position)
                            context.startActivity(intent)
                        }
                        4 -> {
                            val intent = Intent(context, HydroponicCrops::class.java)
                            //intent.putExtra("selectedItem", position)
                            context.startActivity(intent)
                        }
                        5 -> {
                            val intent = Intent(context, HydroponicSystems::class.java)
                            //intent.putExtra("selectedItem", position)
                            context.startActivity(intent)
                        }
                        else -> {

                        }
                    }
                }
            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }



}