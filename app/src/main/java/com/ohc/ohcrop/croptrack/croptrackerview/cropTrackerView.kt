package com.ohc.ohcrop.croptrack.croptrackerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.ohc.ohcrop.CropTracker
import com.ohc.ohcrop.Dashboard
import com.ohc.ohcrop.Profile
import com.ohc.ohcrop.R
import com.ohc.ohcrop.databinding.ActivityCropTrackerAddBinding
import com.ohc.ohcrop.databinding.ActivityCropTrackerViewBinding
import com.ohc.ohcrop.utils.FirebaseUtils

class cropTrackerView : AppCompatActivity() {
    private lateinit var binding: ActivityCropTrackerViewBinding
    private lateinit var userID: String
    private lateinit var referenceId: String

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_tracker_view)

        binding = ActivityCropTrackerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid
        referenceId = intent.getStringExtra("referenceID").toString()

        binding.imageBtnBack.setOnClickListener {
            startActivity(Intent(this, CropTracker::class.java))
            finish()
        }
        binding.imageBtnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            finish()
        }

        //binding.nodNameText.setText(referenceId)
        fetchDataOnFirebase()

        binding.deleteButton.setOnClickListener {
            deleteDocumentFirestore()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event
                val intent = Intent(this@cropTrackerView, CropTracker::class.java)
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

    private fun deleteDocumentFirestore() {
        binding.progressBar.visibility = View.VISIBLE
        val db = FirebaseUtils.firestore
        val userReportsRef = db.collection("user").document(userID).collection("croptrack")
        val query = userReportsRef.whereEqualTo("referenceID", referenceId)

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Check if the document contains the field "referenceID"
                    if (document.contains("referenceID")) {
                        // Delete the document
                        db.collection("user").document(userID).collection("croptrack")
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                // Document successfully deleted
                                println("Document successfully deleted")
                                startActivity(Intent(this, CropTracker::class.java))
                                finish()
                                binding.progressBar.visibility = View.GONE
                            }
                            .addOnFailureListener { e ->
                                // Log the error message if deletion fails
                                println("Error deleting document: $e")
                            }
                    } else {
                        // Log a message indicating that the document does not contain the field "referenceID"
                        println("Document does not contain the field 'referenceID'")
                    }
                }
            }
            .addOnFailureListener { e ->
                // Log the error message if query fails
                println("Error getting documents: $e")
            }
    }


    private fun fetchDataOnFirebase() {
//        val db = FirebaseUtils.firestore
//        val query = db.collection("user").document(userID).collection("croptrack").whereEqualTo("referenceID", referenceId)

        val db = FirebaseUtils.firestore
        val userReportsRef = db.collection("user").document(userID).collection("croptrack")
        val query = userReportsRef.whereEqualTo("referenceID", referenceId)

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.nodNameText.setText(document.getString("nodeName"))
                    binding.noofCropText.setText(document.getLong("noOfCrops").toString())
                    binding.typeofPlantText.setText(document.getString("typeofplant"))

                    binding.sowingText.setText(document.getString("sowingDate"))
                    binding.sowingDurationText.setText(document.getLong("sowingDuration").toString())
                    binding.endSowingText.setText(document.getString("sowingEndDate"))

                    binding.transplantText.setText(document.getString("transplantDate"))
                    binding.transplantDurationText.setText(document.getLong("transplantDuration").toString())
                    binding.endTransplantText.setText(document.getString("transplantEndDate"))
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occurred during the fetch operation
                Log.e("Firestore", "Error getting documents: ", exception)
                // Display a toast or message to inform the user about the error
            }

    }
}