package com.example.ctvms

import CustomSpinnerAdapterBlack
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import customAlert

class addPlan : AppCompatActivity() {
    var added:Boolean = false
    val dbreff = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan)

        val ottspinner = findViewById<Spinner>(R.id.planOTTSpinner)
        val plantypespinner = findViewById<Spinner>(R.id.planTypeSpinner)
        val addPlanBtn = findViewById<Button>(R.id.addPlanBtn)
        val planid = findViewById<TextView>(R.id.planIDValue)
        generateNextPlanId().addOnSuccessListener { generatedPlanId ->
            // Update the TextView with the generated plan ID
            planid.text = generatedPlanId.toString()
        }.addOnFailureListener { exception ->
            // Handle failure
            showCustomAlert("Error", "Failed to generate plan ID: ${exception.message}", R.drawable.error)
        }
        val planname = findViewById<TextView>(R.id.planNameValue)
        val planchannelcount = findViewById<TextView>(R.id.planChannelValue)
        val planprice = findViewById<TextView>(R.id.planPriceValue)
        val ottvalues = arrayOf("Select","Yes","No")
        val plantypes = arrayOf("Select","Base Plan","Add-On Plan")
        val fontPath = R.font.poppins
        val ottadapter = CustomSpinnerAdapterBlack(this, ottvalues,fontPath)
        ottspinner.adapter = ottadapter
        val plantypeadapter = CustomSpinnerAdapterBlack(this, plantypes,fontPath)
        plantypespinner.adapter = plantypeadapter
        addPlanBtn.setOnClickListener(){
            val planIDValue = planid.text.toString()
            val planNameValue = planname.text.toString()
            val planChannelValue = planchannelcount.text.toString()
            val planOTTValue = ottspinner.selectedItem.toString()
            val planPriceValue = planprice.text.toString()
            val plantype = plantypespinner.selectedItem.toString()
            if (plantype == "Select"){
                showCustomAlert("Error","Please Select Plan Type",R.drawable.errorinfo)
            }else if (planNameValue.isEmpty()){
                showCustomAlert("Error","Please Enter Plan Name",R.drawable.errorinfo)
            }else if (planChannelValue.isEmpty()){
                showCustomAlert("Error","Please Enter Channel Count",R.drawable.errorinfo)
            }else if (planChannelValue.toInt() <= 0){
                showCustomAlert("Error","Channel Count Should Be Greater Than 0",R.drawable.errorinfo)
            }else if (planOTTValue == "Select"){
                showCustomAlert("Error","Please Select OTT Status",R.drawable.errorinfo)
            }else if (planPriceValue.isEmpty()){
                showCustomAlert("Error","Please Enter Plan Price",R.drawable.errorinfo)
            }else{
                if (!NetworkUtils.isNetworkAvailable(this)) {
                    NetworkUtils.showNetworkAlert(this)
                } else {
                    val db = Firebase.firestore
                    val collectionPath = "/admins/abyjose377@gmail.com/Plans"
                    val planData = hashMapOf(
                        "planID" to planIDValue,
                        "planType" to plantype,
                        "planName" to planNameValue,
                        "channelCount" to planChannelValue,
                        "ott" to planOTTValue,
                        "price" to planPriceValue
                    )
                    db.document("$collectionPath/$planIDValue")
                        .set(planData)
                        .addOnSuccessListener {
                            added = true
                            Log.d(TAG, "DocumentSnapshot added with ID: $planIDValue")
                            showCustomAlert("Success","New Plan Added Successfully.",R.drawable.success)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                            showCustomAlert("Error","Something Went Wrong. Try Again.",R.drawable.error)
                        }
                }
            }
        }
    }
    fun showCustomAlert(alertTitle:String,alertMessage: String,alertIcon: Int) {
        val myCustomAlert = customAlert(this)
        myCustomAlert.setData(alertTitle, alertIcon, alertMessage)
        val customAlertView = myCustomAlert.getView()
        val alert = AlertDialog.Builder(this)
            .setView(customAlertView)
            .create()
        val okBtn: Button = myCustomAlert.getOkButton()
        alert.show()
        okBtn.setOnClickListener {
            if (!added){
                alert.dismiss()
            }else{
                alert.dismiss()
                finish()
            }

        }
    }
    fun generateNextPlanId(): Task<String> {
        // Initialize Firestore
        val db = Firebase.firestore

        // Reference to the collection
        val collectionRef = db.collection("/admins/abyjose377@gmail.com/Plans")

        // Query to get the maximum plan ID
        return collectionRef
            .orderBy(FieldPath.documentId(), com.google.firebase.firestore.Query.Direction.DESCENDING) // Ordering by document ID
            .limit(1)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && !documents.isEmpty()) {
                        val maxId = documents[0].id.toInt()
                        val nextId = maxId + 1
                        String.format("%06d", nextId)
                    } else {
                        "000001" // If no documents found, start from 000001
                    }
                } else {
                    // Handle failure
                    println("Error getting documents: ${task.exception}")
                    null
                }
            }
    }
}