package com.ohc.ohcrop.control

import android.content.ContentValues
import android.util.Log
import com.ohc.ohcrop.utils.FirebaseUtils
import kotlinx.coroutines.tasks.await

object ControlDocumentUtils {

    private val userID: String by lazy {
        FirebaseUtils.firebaseAuth.currentUser!!.uid
    }

    fun createCollectionControl() {
        val controlRef = FirebaseUtils.firestore.collection("user").document(userID).collection("control")

        // Check if the "control" collection exists
        controlRef.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                // "control" collection doesn't exist, create it
                FirebaseUtils.firestore.collection("user").document(userID).set(hashMapOf<String, Any>())
                    .addOnSuccessListener {
                        println("Control collection created successfully")
                    }.addOnFailureListener { exception ->
                        println("Error creating control collection: $exception")
                    }
            } else {
                println("Control collection already exists")
            }
        }.addOnFailureListener { exception ->
            println("Error checking control collection existence: $exception")
        }
    }

    fun createControlDocuments() {
        val userRef = FirebaseUtils.firestore.collection("user").document(userID)
        val controlRef = userRef.collection("control")

        val documents = listOf("watertank", "misting", "irrigation", "fan", "light")

        for (documentName in documents) {
            val docRef = controlRef.document(documentName)

            // Check if the document exists
            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    // Document doesn't exist, create it
                    val data = hashMapOf<String, Any>() // You can add any initial data here if needed
                    docRef.set(data)
                        .addOnSuccessListener {
                            println("$documentName document created successfully")
                        }.addOnFailureListener { exception ->
                            println("Error creating $documentName document: $exception")
                        }
                } else {
                    println("$documentName document already exists")
                }
            }.addOnFailureListener { exception ->
                println("Error checking $documentName document existence: $exception")
            }
        }
    }


    fun createWaterTankDocumentData(): Map<String, Any> {
        return hashMapOf(
            "controlmode" to false,
            "manualmode" to false
        )
    }
    fun createWaterTankDocument() {
        val controlRef = FirebaseUtils.firestore.collection("user").document(userID).collection("control")
        val watertankRef = controlRef.document("watertank")

        watertankRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                watertankRef.set(createWaterTankDocumentData())
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Watertank document created successfully")
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error creating watertank document", exception)
                    }
            } else {
                Log.d(ContentValues.TAG, "Watertank document already exists")
            }
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error checking watertank document existence", exception)
        }
    }

    fun createMistingDocumentData(): Map<String, Any> {
        return hashMapOf(
            "controlmode" to false,
            "manualmode" to false,
            "from" to 0,
            "to" to 0,
            "duration" to 0,
            "indexFrom" to 0,
            "indexTo" to 0,
            "indexDuration" to 0
        )
    }
    fun createMistingDocument() {
        val controlRef = FirebaseUtils.firestore.collection("user").document(userID).collection("control")
        val mistingRef = controlRef.document("misting")

        mistingRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                mistingRef.set(createMistingDocumentData())
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Control document created successfully")
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error creating control document", exception)
                    }
            } else {
                Log.d(ContentValues.TAG, "Control document already exists")
            }
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error checking control document existence", exception)
        }
    }

    fun createIrrigationDocumentData(): Map<String, Any> {
        return hashMapOf(
            "controlmode" to false,
            "manualmode" to false,
            "from" to 0,
            "to" to 0,
            "duration" to 0,
            "indexFrom" to 0,
            "indexTo" to 0,
            "indexDuration" to 0
        )
    }
    fun createIrrigationDocument() {
        val controlRef = FirebaseUtils.firestore.collection("user").document(userID).collection("control")
        val watertankRef = controlRef.document("irrigation")

        watertankRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                watertankRef.set(createIrrigationDocumentData())
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Control document created successfully")
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error creating control document", exception)
                    }
            } else {
                Log.d(ContentValues.TAG, "Control document already exists")
            }
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error checking control document existence", exception)
        }
    }

    fun createFanDocumentData(): Map<String, Any> {
        return hashMapOf(
            "controlmode" to false,
            "manualmode" to false,
            "from" to 0,
            "to" to 0,
            "duration" to 0,
            "indexFrom" to 0,
            "indexTo" to 0,
            "indexDuration" to 0
        )
    }
    fun createFanDocument() {
        val controlRef = FirebaseUtils.firestore.collection("user").document(userID).collection("control")
        val watertankRef = controlRef.document("fan")

        watertankRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                watertankRef.set(createFanDocumentData())
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Control document created successfully")
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error creating control document", exception)
                    }
            } else {
                Log.d(ContentValues.TAG, "Control document already exists")
            }
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error checking control document existence", exception)
        }
    }

    fun createLightDocumentData(): Map<String, Any> {
        return hashMapOf(
            "controlmode" to false,
            "manualmode" to false,
            "from" to 0,
            "to" to 0,
            "duration" to 0,
            "indexFrom" to 0,
            "indexTo" to 0,
            "indexDuration" to 0
        )
    }
    fun createLightDocument() {
        val controlRef = FirebaseUtils.firestore.collection("user").document(userID).collection("control")
        val watertankRef = controlRef.document("light")

        watertankRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                watertankRef.set(createLightDocumentData())
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Control document created successfully")
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error creating control document", exception)
                    }
            } else {
                Log.d(ContentValues.TAG, "Control document already exists")
            }
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error checking control document existence", exception)
        }
    }



}