package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils.firebaseAuth
import com.ohc.ohcrop.utils.FirebaseUtils.firebaseUser
import com.ohc.ohcrop.utils.FirebaseUtils.firestore


class Register : AppCompatActivity() {

    private lateinit var registerSignInButton: Button
    //private lateinit var auth: FirebaseAuth
    private lateinit var registerName: EditText
    private lateinit var registerPhone: EditText
    private lateinit var registerEmail: EditText
    private lateinit var registerPass: EditText
    private lateinit var registerCPass: EditText
    private lateinit var registerButton: Button
    private lateinit var errorText: TextView

    private lateinit var userName: String
    private lateinit var userPhone: String
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    lateinit var createAccountInputsArray: Array<EditText>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

//        auth = FirebaseAuth.getInstance()
        registerName = findViewById(R.id.RegisterNameText)
        registerPhone = findViewById(R.id.RegisterPhoneText)
        registerEmail = findViewById(R.id.RegisterEmailText)
        registerPass = findViewById(R.id.RegisterPassword)
        registerButton = findViewById(R.id.RegisterBtn)
        errorText = findViewById(R.id.RegisterTextError)
        registerCPass = findViewById(R.id.RegisterConfirmPassword)
        registerSignInButton = findViewById(R.id.registerSignInButton)

        createAccountInputsArray = arrayOf(registerEmail, registerPass, registerCPass)

        registerButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View){
                signIn()
            }
        })

        registerSignInButton.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            toast("please sign into your account")
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, Dashboard::class.java))
            toast("welcome back!!!...")
        }
    }

    private fun notEmpty(): Boolean = registerEmail.text.toString().trim().isNotEmpty() &&
            registerPass.text.toString().trim().isNotEmpty() &&
            registerCPass.text.toString().trim().isNotEmpty() &&
            registerName.text.toString().trim().isNotEmpty() &&
            registerPhone.text.toString().trim().isNotEmpty()


    private fun signIn() {
        if (identicalPassword()) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            userName = registerName.text.toString()
            userPhone = registerPhone.text.toString().trim()
            userEmail = registerEmail.text.toString().trim()
            userPassword = registerPass.text.toString().trim()



            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toast("Created Account Successfully !")
                        sendEmailVerification()

                        //firebase collection
                        val userID = firebaseAuth.currentUser!!.uid //get auth user ID
                        //val user = mapOf("email" to firebaseAuth.currentUser!!.email)// get auth email

                        val userMap = hashMapOf(
                            "name" to userName,
                            "phone" to userPhone,
                            "userEmail" to userEmail,
                            "password" to userPassword,
                        )

                        firestore.collection("user").document(userID).set(userMap)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document",e) }


//                        val userRef = firestore.collection("user")//put this on collection if collection does not exists will create one
//                        userRef.document(userID).set(user).addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document",e) }

                        startActivity(Intent(this, Dashboard::class.java))
                        finish()
                    } else {
                        toast("failed to Authenticate !")
                    }
                }
        }
    }

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            registerPass.text.toString().trim() == registerCPass.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {
            toast("passwords are not matching !")
        }
        return identical
    }

    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */
    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("email sent to $userEmail")
                }
            }
        }
    }
}