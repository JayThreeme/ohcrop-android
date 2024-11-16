package com.ohc.ohcrop.control

import android.app.ActivityOptions
import android.content.ContentValues
import android.content.Intent
import android.hardware.lights.Light
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ohc.ohcrop.R
import com.ohc.ohcrop.control.fan.FanControl
import com.ohc.ohcrop.control.irrigation.IrrigationControl
import com.ohc.ohcrop.control.light.LightControl
import com.ohc.ohcrop.control.misting.MistingControl
import com.ohc.ohcrop.howto.appnavigation.AppNavigation
import com.ohc.ohcrop.howto.monitorandcontrol.MonitoringAndControl
import com.ohc.ohcrop.howto.monitoringsensors.MonitoringSensors
import com.ohc.ohcrop.utils.FirebaseUtils
import com.ohc.ohcrop.control.watertank.WaterTankControl

@Suppress("DEPRECATION")
class RecyclerControlAdapter: RecyclerView.Adapter<RecyclerControlAdapter.ViewHolder>() {
    private lateinit var userID: String
    private var title = arrayOf("Water Tank", "Misting", "Irrigation", "Fan", "Light")
    private var details = arrayOf("Backup Refill Tank Motor", "Misting Motor Control", "Irrigation Motor Control", "Fan Motor Control", "Light Control")
    private var images = intArrayOf(R.drawable.watertank, R.drawable.misting, R.drawable.irrigation, R.drawable.fan, R.drawable.light)
    //private var switch = booleanArrayOf(true,true,true,true,true,true)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.control_card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemDetail.text = details[position]
        holder.itemImage.setImageResource(images[position])
        //holder.itemswitch.isChecked = switch[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView
        //lateinit var itemswitch: Switch

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
            //itemswitch = itemView.findViewById(R.id.item_switch)
            userID = FirebaseUtils.firebaseAuth.currentUser!!.uid


            itemView.setOnClickListener{
                val position: Int = adapterPosition
                //Toast.makeText(itemView.context, "you clicked ${title[position] + position.toString()}", Toast.LENGTH_SHORT).show()
            }

            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    val context = itemView.context
                    val intent = when (position) {
                        0 -> Intent(context, WaterTankControl::class.java)
                        1 -> Intent(context, MistingControl::class.java)
                        2 -> Intent(context, IrrigationControl::class.java)
                        3 -> Intent(context, FanControl::class.java)
                        4 -> Intent(context, LightControl::class.java)
                        else -> null
                    }

                    intent?.let {
                        // Apply custom animation
                        val enterAnimation = R.anim.slide_in_right
                        val exitAnimation = R.anim.slide_out_left
                        val options = ActivityOptions.makeCustomAnimation(context, enterAnimation, exitAnimation)
                        context.startActivity(it, options.toBundle())
                }
                //Toast.makeText(itemView.context, "you clicked " + "${position}", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}