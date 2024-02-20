package com.example.ctvms

import CustomSpinnerAdapterBlack
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import customAlert

class dueCheckerDetais : Fragment() {
    private lateinit var CustCrf: TextView
    private lateinit var CustName: TextView
    private var totalAmount:Int = 0
    private lateinit var janValue: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_due_checker_detais, container, false)
        CustCrf = view.findViewById(R.id.DuecrfValue)
        CustName = view.findViewById(R.id.DueCusNameValue)
        janValue = view.findViewById(R.id.janValue)
        val FetchBtn = view.findViewById<Button>(R.id.fetchBtn)

        // Retrieve the crfValue from arguments
        val crf = arguments?.getString("crfValue", "") ?: ""
        val custName = arguments?.getString("cusName", "") ?: ""
        CustCrf.text = crf
        CustName.text = custName
        var yearSpinner = view.findViewById<AppCompatSpinner>(R.id.DueYearValue)
        val db = Firebase.firestore
        val crfNumber = CustCrf.text.toString() // Example CRF number
        val yearselectionpath = "/admins/abyjose377@gmail.com/customers/$crfNumber/payment"
        var yearlist = arrayOf<String>("Select")

// Query the payment collection
        db.collection(yearselectionpath)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    // Access document id using document.id
                    val documentId = document.id
                    yearlist = yearlist + documentId
                    val fontPath = R.font.poppins
                    val yearAdapter = CustomSpinnerAdapterBlack(
                        requireContext(),
                        yearlist,
                        fontPath
                    )
                    yearSpinner.adapter = yearAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                showCustomAlert("Error","Something Went Wrong. Try Again",R.drawable.error)
            }
        FetchBtn.setOnClickListener(){
            if (yearSpinner.selectedItem == "Select"){
                showCustomAlert("Warning","Please Select Year",R.drawable.errorinfo)
            }else{
                if (!NetworkUtils.isNetworkAvailable(requireContext())) {
                    NetworkUtils.showNetworkAlert(requireContext())
                } else {
                    val db = Firebase.firestore
                    val crfNumber = CustCrf.text.toString()
                    val year = yearSpinner.selectedItem.toString()
                    // Construct the path to the document
                    val documentPath = "/admins/abyjose377@gmail.com/customers/$crfNumber/payment/$year"
                    // Get the document reference
                    val docRef = db.document(documentPath)
                    // Fetch the document
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val data = document.data
                                for (month in listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")) {
                                    val status = data?.get(month) as String? // Assuming status is stored as a String
                                    val monthValueTextView = view.findViewWithTag<TextView>("${month.lowercase()}Value")
                                    if (status != null && monthValueTextView != null) {
                                        if (status == "Not Paid") {
                                            totalAmount += 250
                                            monthValueTextView.text = "NOT PAID"
                                            monthValueTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.pendingTextColor))
                                        } else if (status == "Paid") {
                                            monthValueTextView.text = "PAID"
                                            monthValueTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.paidTextColor))
                                        } else {
                                            monthValueTextView.text = "NILL"
                                            monthValueTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.nillTextColor))
                                        }
                                    } else {
                                        Log.e(TAG, "TextView or status is null for $month")
                                        showCustomAlert("Error","Something Went Wrong. Try Again",R.drawable.error)
                                    }
                                }
                                showCustomAlert("Success","Data Fetched Successfully.",R.drawable.success)
                                val outstanding = view.findViewById<TextView>(R.id.DueCusOutStandingValue)
                                outstanding.text = "₹ $totalAmount"
                            } else {
                                Log.d(TAG, "Document not found")
                                showCustomAlert("Caution","No Data Found",R.drawable.errorinfo)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error getting document", exception)
                            showCustomAlert("Error","Something Went Wrong. Try Again",R.drawable.error)
                        }
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up other views and listeners as needed
    }
    fun showCustomAlert(alertTitle:String,alertMessage: String,alertIcon: Int) {
        val myCustomAlert = customAlert(requireContext())
        myCustomAlert.setData(alertTitle, alertIcon, alertMessage)
        val customAlertView = myCustomAlert.getView()
        val alert = AlertDialog.Builder(context)
            .setView(customAlertView)
            .create()
        val okBtn: Button = myCustomAlert.getOkButton()
        alert.show()
        okBtn.setOnClickListener {
            alert.dismiss()
        }
    }
}