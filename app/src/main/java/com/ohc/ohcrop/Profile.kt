package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils

class Profile : AppCompatActivity() {

    private lateinit var backButton : ImageButton

    private lateinit var p_name : TextView
    private lateinit var p_pass : TextView
    private lateinit var p_phone : TextView
    private lateinit var p_email : TextView
    private lateinit var logOutButton : Button
    private lateinit var userID: String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        backButton = findViewById(R.id.imageBtnBack)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid

        logOutButton = findViewById(R.id.dashboardLogoutBtn)
        p_name = findViewById(R.id.p_name)
        p_pass = findViewById(R.id.p_pass)
        p_phone = findViewById(R.id.p_phone)
        p_email = findViewById(R.id.p_email)

        setProfile()

        backButton.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        logOutButton.setOnClickListener {
            FirebaseUtils.firebaseAuth.signOut()
            startActivity(Intent(this, Login::class.java))
            toast("signed out")
            finish()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun setProfile() {//Get Data on firebase
        val ref = FirebaseUtils.firestore.collection("user").document(userID)

            ref.get().addOnSuccessListener {documentSnapshot  ->

                var name = documentSnapshot.data?.get("name").toString()
                var pass = documentSnapshot.data?.get("password").toString()
                var phone = documentSnapshot.data?.get("phone").toString()
                var email = documentSnapshot.data?.get("userEmail").toString()


                p_name.text = name
                p_pass.text = pass
                p_phone.text = phone
                p_email.text = email
        }.addOnFailureListener {
                    e -> Log.w(ContentValues.TAG, "Error writing document",e)
        }
    }
}