package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ohc.ohcrop.databinding.ActivityTestBinding


class Test : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityTestBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_test)

        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.TestButtonRead.setOnClickListener { readData() }
        binding.TestButtonValue.setOnClickListener { setValueData() }
        databaseListener()

        val backtomain = findViewById<Button>(R.id.TestBtnBackToMain)
        backtomain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext,"Test ESP32 + RTDB + Mobile App", Toast.LENGTH_SHORT).show()
        }
    }

    private fun databaseListener() {
        database = FirebaseDatabase.getInstance().getReference()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val voltage = snapshot.child("Sensor/voltage").value
                binding.textViewVoltage.setText(voltage.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Test, "Failed to read sensor data", Toast.LENGTH_SHORT).show()
            }
        }
        database.addValueEventListener(postListener)
    }

    private fun readData() {
        database = FirebaseDatabase.getInstance().getReference("Sensor")
        database.child("voltage").get().addOnSuccessListener {
            if (it.exists()){
                val voltage:Float = it.value.toString().toFloat()
                Toast.makeText(this, "Successful Volatage Read", Toast.LENGTH_SHORT).show()
                binding.textViewVoltage.setText(voltage.toString())
            }else{
                Toast.makeText(this, "Successful/Volatage path does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setValueData() {
        var pwmValue:Int = 0
        try {
            pwmValue = binding.TestEditTextPWM.text.toString().toInt()
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            return
        }

        database = FirebaseDatabase.getInstance().getReference("LED")
        database.child("analog").setValue(pwmValue).addOnSuccessListener {
            Toast.makeText(this, "PWN Set Successful", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to set PWM value", Toast.LENGTH_SHORT).show()
        }
    }

}