package com.example.ctvms

import CustomSpinnerAdapterBlack
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ctvms.adapter.managePlansAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import customAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class editPlanFrag : Fragment(){
    private lateinit var fullId: String
    private lateinit var adapter2: managePlansAdapter
    private var firstPlan: Plans? = null
    private lateinit var planIDTextView: TextView
    private lateinit var planTypeSpinner: Spinner
    private lateinit var planNameTextView: TextInputEditText
    private lateinit var planChannelCountTextView: TextInputEditText
    private lateinit var planOTTSpinner: Spinner
    private lateinit var planPriceTextView: TextInputEditText
    private lateinit var saveBtn: Button
    private var updated: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_edit_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        planTypeSpinner = view.findViewById<Spinner>(R.id.planTypeSpinner)
        val plantypes = arrayOf("Select","Base Plan","Add-On Plan")
        val fontPath = R.font.poppins
        val plantypeadapter = CustomSpinnerAdapterBlack(requireContext(),plantypes,fontPath)
        planTypeSpinner.adapter = plantypeadapter
        planOTTSpinner = view.findViewById<Spinner>(R.id.planOTTSpinner)
        val ottTypes = arrayOf("Select","Yes","No")
        val Ottadapter = CustomSpinnerAdapterBlack(requireContext(),ottTypes,fontPath)
        planOTTSpinner.adapter = Ottadapter
        planNameTextView = view.findViewById<TextInputEditText>(R.id.planNameValue)
        planChannelCountTextView = view.findViewById<TextInputEditText>(R.id.planChannelValue)
        planPriceTextView = view.findViewById<TextInputEditText>(R.id.planPriceValue)
        planIDTextView = view.findViewById<TextView>(R.id.planIDValue)
        saveBtn = view.findViewById(R.id.updatePlanBtn)
        fullId = arguments?.getString(ARG_FULL_ID).toString()
        planIDTextView.text = fullId
        fetchPlansDataFromFirestore(fullId)

        saveBtn.setOnClickListener() {
            val currentPlanName = planNameTextView.text.toString()
            val currentPlanChannelCount = planChannelCountTextView.text.toString()
            val currentPlanPrice = planPriceTextView.text.toString()
            val currentPlanType = planTypeSpinner.selectedItem.toString()
            val currentPlanOTT = planOTTSpinner.selectedItem.toString()

            if (currentPlanName != firstPlan?.planName ||
                currentPlanChannelCount != firstPlan?.channelCount ||
                currentPlanPrice != firstPlan?.planPrice ||
                currentPlanType != firstPlan?.planType ||
                currentPlanOTT != firstPlan?.planOTT
            ) {

                if (!NetworkUtils.isNetworkAvailable(requireContext())) {
                    NetworkUtils.showNetworkAlert(requireContext())
                    return@setOnClickListener
                }

                // Create a map of fields to update
                val updates = hashMapOf<String, Any>(
                    "planName" to currentPlanName,
                    "channelCount" to currentPlanChannelCount,
                    "price" to currentPlanPrice,
                    "planType" to currentPlanType,
                    "ott" to currentPlanOTT
                )

                // Get the Firestore instance
                val db = Firebase.firestore

                // Update the document in Firestore
                db.collection("/admins/abyjose377@gmail.com/Plans")
                    .document(fullId)
                    .update(updates)
                    .addOnSuccessListener {
                        // Update successful
                        updated = true
                        showCustomAlert("Success", "Plan Details Updated Successfully.", R.drawable.success)
                    }
                    .addOnFailureListener { e ->
                        // Update failed
                        println("Error updating document: $e")
                        // Show an error message to the user
                        showCustomAlert("Error", "Failed to update plan data", R.drawable.error)
                    }
            } else {
                // Data has not changed
                showCustomAlert("Alert","No Changes Found.",R.drawable.errorinfo)
            }
        }
    }
        companion object {
        private const val ARG_FULL_ID = "fullId"
        fun newInstance(fullId: String): editPlanFrag {
            val fragment = editPlanFrag()
            val args = Bundle()
            args.putString(ARG_FULL_ID, fullId)
            fragment.arguments = args
            return fragment
        }
    }
    private fun fetchPlansDataFromFirestore(planId: String) {
        val db = Firebase.firestore
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val plansList = mutableListOf<Plans>()
                val collectionReference = db.collection("/admins/abyjose377@gmail.com/Plans")
                val querySnapshot = collectionReference.whereEqualTo("planID", planId).get().await()

                if (!querySnapshot.isEmpty) { // Check if the query returned any documents
                    for (document in querySnapshot.documents) {
                        val planIDValue = document.getString("planID") ?: ""
                        val planType = document.getString("planType") ?: ""
                        val planNameValue = document.getString("planName") ?: ""
                        val planChannelValue = document.getString("channelCount") ?: ""
                        val planOTTValue = document.getString("ott") ?: ""
                        val planPriceValue = document.getString("price") ?: ""

                        // Create a Plan object with only the required fields
                        val plan = Plans(planNameValue, planIDValue, planChannelValue, R.drawable.plan_bg, planPriceValue, planId,planType,planOTTValue)
                        plansList.add(plan)
                    }

                    // Update UI on the main thread
                    launch(Dispatchers.Main) {
                        // Retrieve the first plan from the list
                        firstPlan = plansList.first()

                        // Set data to UI elements
                        planIDTextView.text = firstPlan!!.planID
                        planNameTextView.setText(firstPlan!!.planName)
                        planChannelCountTextView.setText(firstPlan!!.channelCount)
                        planPriceTextView.setText(firstPlan!!.planPrice)

                        // Set selected item in spinners
                        (planTypeSpinner.adapter as? ArrayAdapter<String>)?.apply {
                            val position = getPosition(firstPlan!!.planType)
                            planTypeSpinner.setSelection(position)
                        }

                        (planOTTSpinner.adapter as? ArrayAdapter<String>)?.apply {
                            val position = getPosition(firstPlan!!.planOTT)
                            planOTTSpinner.setSelection(position)
                        }
                    }
                } else {
                    // Handle case where the document does not exist
                    println("Document with plan ID $planId does not exist")
                    // Show an appropriate message to the user
                    showCustomAlert("Error", "Document with plan ID $planId does not exist", R.drawable.errorinfo)
                }
            } catch (e: Exception) {
                // Handle exceptions
                println("Error fetching plans data: ${e.message}")
                // Show error message to the user
                showCustomAlert("Error", "Failed to fetch plans data", R.drawable.errorinfo)
            }
        }
    }

    private fun showCustomAlert(alertTitle: String, alertMessage: String, alertIcon: Int) {
        val myCustomAlert = customAlert(requireContext())
        myCustomAlert.setData(alertTitle, alertIcon, alertMessage)
        val customAlertView = myCustomAlert.getView()
        val alert = AlertDialog.Builder(requireContext())
            .setView(customAlertView)
            .create()
        val okBtn: Button = myCustomAlert.getOkButton()
        alert.show()
        okBtn.setOnClickListener {
            if (!updated){
                alert.dismiss()
            }else{
                alert.dismiss()
                requireActivity().finish()
            }
        }
    }

}