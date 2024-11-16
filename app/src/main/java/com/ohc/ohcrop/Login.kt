package com.ohc.ohcrop

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.ohc.ohcrop.utils.FirebaseUtils
import com.ohc.ohcrop.utils.Extensions
import com.ohc.ohcrop.utils.Extensions.toast
import com.ohc.ohcrop.utils.FirebaseUtils.firebaseAuth

class Login : AppCompatActivity() {
    //firebase auth
    private lateinit var auth: FirebaseAuth

    //gobal variables
    private  lateinit var signupEmail: EditText
    private lateinit var signupPass: EditText
    private lateinit var loginButton: Button
    private lateinit var loginRedirectText: TextView
    private lateinit var registerButton: Button
    private lateinit var loginTextError: TextView

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        signupEmail = findViewById(R.id.LoginUsernameText)
        signupPass = findViewById(R.id.LoginPasswordText)
        loginButton = findViewById(R.id.LoginBtn)
        registerButton = findViewById(R.id.LoginRegisterBtn)
        loginRedirectText = findViewById(R.id.LoginTextError)
        loginTextError = findViewById(R.id.LoginTextError)

        signInInputsArray = arrayOf(signupEmail, signupPass)

//        var email:String
//        var password:String

        //Login Button
        loginButton.setOnClickListener {
            signInUser()
        }

        //Register Button
        registerButton.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            Toast.makeText(applicationContext,"Register!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {

    signInEmail = signupEmail.text.toString().trim()
    signInPassword = signupPass.text.toString().trim()

    if (notEmpty()) {
        firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
            .addOnCompleteListener { signIn ->
                if (signIn.isSuccessful) {
                    startActivity(Intent(this, Dashboard::class.java))
                    toast("signed in successfully")
                    finish()
                } else {
                    toast("sign in failed")
                }
            }
    } else {
        signInInputsArray.forEach { input ->
            if (input.text.toString().trim().isEmpty()) {
                input.error = "${input.hint} is required"
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, Dashboard::class.java))
            toast("Welcome back!!!...")
        }
    }
}

//    private fun signin(email: String, password: String) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    //Log.d(TAG, "signInWithEmail:success")
//                    val user = auth.currentUser
//                    //updateUI(user)
//
//                    val intent = Intent(this, Dashboard::class.java)
//                    startActivity(intent)
//                    Toast.makeText(applicationContext,"Login Success!", Toast.LENGTH_SHORT).show()
//                } else {
//                    // If sign in fails, display a message to the user.
//                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
//                    signupEmail.text.clear()
//                    signupPass.text.clear()
//                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()
//                    //updateUI(null)
//                }
//            }
//    }

//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            reload()
//        }
//    }

//    private fun reload() {
//        //refresh view
//    }

//    override fun onStart() {
//        super.onStart()
//        val user: FirebaseUser? = FirebaseUtils.firebaseAuth.currentUser
//        user?.let {
//            startActivity(Intent(this, Dashboard::class.java))
//            toast("welcome back!!!...")
//        }
//    }

//}