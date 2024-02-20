package com.example.ctvms

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import customAlert
import java.time.LocalDate

class addCustomer : AppCompatActivity(), customerPersonalInfo.OnButtonClickListener, serviceDetails.OnAddButtonClickListener {
    val adminEmail = "abyjose377@gmail.com"
    val firestore = FirebaseFirestore.getInstance()
    private lateinit var cusNameValue: String
    private lateinit var cusEmailValue: String
    private lateinit var cusMobileValue: String
    private lateinit var cusGenderValue: String
    private lateinit var cusDOBValue: String
    private lateinit var cusAddressValue: String
    private lateinit var cusCityValue: String
    private lateinit var cusDistrictValue: String
    private lateinit var cusStateValue: String
    private lateinit var cusBasePlanValue: String
    private lateinit var cusChipIDValue: String
    private lateinit var cusInstallationDateValue: String
    private lateinit var cusPlanStartDateValue: String
    private lateinit var cusAddOn1Value: String
    private lateinit var cusAddOn2Value: String
    private lateinit var cusAddOn3Value: String
    private lateinit var cusAddOn4Value: String
    private lateinit var custCFFValue:String
    private var addcus:Boolean = false
    private lateinit var serviceimg: ImageView
    private lateinit var serviceimgline: ImageView
    private lateinit var personalimg: ImageView
    private lateinit var personalimgline: ImageView
    private lateinit var confirmimg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        val backbtn = findViewById<ImageView>(R.id.backBtn)
        backbtn.setOnClickListener(){
            onBackPressed()
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val cusdetailsfrag = customerPersonalInfo()
        cusdetailsfrag.setOnButtonClickListener(this)
        fragmentTransaction.replace(R.id.addCusContainer,cusdetailsfrag)
        fragmentTransaction.commit()
    }
    fun generateUniqueCRFNO(adminEmail: String, firestore: FirebaseFirestore, callback: (String) -> Unit) {
        val crfno = generateCRFNO() // Generate a CRFNO
        val crfnoRef = firestore.collection("admins")
            .document(adminEmail)
            .collection("customers")
            .document(crfno)

        crfnoRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    // If the document exists, generate a new CRFNO
                    generateUniqueCRFNO(adminEmail, firestore, callback)
                } else {
                    // CRFNO is unique, return it
                    callback(crfno)
                }
            } else {
                // Error handling
                println("Error checking CRFNO: ${task.exception}")
            }
        }
    }
    var crfnoCounter = 0
    fun generateCRFNO(): String {
        val paddedCounter = String.format("%06d", crfnoCounter++)
        return "CTVMS01$paddedCounter"
    }
    override fun onButtonClicked() {
        val cusData = (supportFragmentManager.findFragmentById(R.id.addCusContainer) as customerPersonalInfo).getCusData()
        if (cusData!=null){
            cusNameValue = cusData.cusName
            cusEmailValue = cusData.cusEmail
            cusMobileValue= cusData.cusMobile
            cusGenderValue = cusData.cusGender
            cusDOBValue = cusData.cusDOB
            cusAddressValue = cusData.cusAddress
            cusCityValue = cusData.cusCity
            cusDistrictValue = cusData.cusDistrict
            cusStateValue = cusData.cusState
            personalimg = findViewById<ImageView>(R.id.fillIcon)
            personalimgline = findViewById<ImageView>(R.id.fillLine)
            personalimg.setImageResource(R.drawable.success)
            personalimgline.setImageResource(R.drawable.greenline)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val serviceFrag = serviceDetails()
            serviceFrag.setOnAddButtonClickListener(this)
            fragmentTransaction.replace(R.id.addCusContainer,serviceFrag)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onAddButtonClicked() {
        val serviceData = (supportFragmentManager.findFragmentById(R.id.addCusContainer) as serviceDetails).getServiceData()
        if (serviceData!=null){
            cusBasePlanValue = serviceData.basePlan
            cusChipIDValue = serviceData.chipID
            cusInstallationDateValue = serviceData.installationDate
            cusPlanStartDateValue = serviceData.planStartDate
            cusAddOn1Value = serviceData.addOn1
            cusAddOn2Value = serviceData.addOn2
            cusAddOn3Value = serviceData.addOn3
            cusAddOn4Value = serviceData.addOn4
            serviceimg = findViewById<ImageView>(R.id.verificationIcon)
            serviceimgline = findViewById<ImageView>(R.id.veificationLine)
            serviceimg.setImageResource(R.drawable.success)
            serviceimgline.setImageResource(R.drawable.greenline)
            customDialog()
            Toast.makeText(this,cusBasePlanValue,Toast.LENGTH_SHORT).show()
        }
    }
    private fun customDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.customdialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val yesBtn: Button = dialog.findViewById(R.id.yesBtn)
        val noBtn: Button = dialog.findViewById(R.id.noBtn)

        yesBtn.setOnClickListener(){
            dialog.dismiss()
            if (!NetworkUtils.isNetworkAvailable(this)) {
                NetworkUtils.showNetworkAlert(this)
            } else {
                saveCusData { success ->
                    if (success){
                        addcus = true
                        confirmimg = findViewById<ImageView>(R.id.confirmIcon)
                        confirmimg.setImageResource(R.drawable.success)
                        showCustomAlert(this,"Success","Customer Added Successfully",R.drawable.success)
                    }else{
                        serviceimg.setImageResource(R.drawable.notcompleted)
                        serviceimgline.setImageResource(R.drawable.line)
                        showCustomAlert(this,"Error","Something Went Wrong. Try Again Later",R.drawable.error)
                    }
                }
            }
        }
        noBtn.setOnClickListener(){
            serviceimg.setImageResource(R.drawable.notcompleted)
            serviceimgline.setImageResource(R.drawable.line)
            dialog.dismiss()
        }
        dialog.show()
    }
    fun saveCusData(callback: (Boolean) -> Unit) {
        // Call generateUniqueCRFNO with the appropriate parameters
        generateUniqueCRFNO(adminEmail, firestore) { uniqueCRFNO ->
            // This block will be executed when a unique CRFNO is generated
            println("Unique CRFNO generated: $uniqueCRFNO")
            // Now you can use the unique CRFNO as needed
            custCFFValue = uniqueCRFNO // Assign the generated CRFNO to custCFFValue

            // Now that you have the CRFNO, you can proceed with saving customer data
            val today = LocalDate.now()
            val year = today.year
            val jan = "Nill"
            val feb = "Nill"
            val mar = "Nill"
            val apr = "Nill"
            val may = "Nill"
            val jun = "Nill"
            val jul = "Nill"
            val aug = "Nill"
            val sep = "Nill"
            val oct = "Nill"
            val nov = "Nill"
            val dec = "Nill"

            val cusPData = hashMapOf(
                "cusCRF" to custCFFValue,
                "cusName" to cusNameValue,
                "cusEmail" to cusEmailValue,
                "cusMobile" to cusMobileValue,
                "cusGender" to cusGenderValue,
                "cusDOB" to cusDOBValue,
                "cusAddress" to cusAddressValue,
                "cusCity" to cusCityValue,
                "cusDistrict" to cusDistrictValue,
                "cusState" to cusStateValue
            )

            val cusSData = hashMapOf(
                "status" to "Active",
                "cusBasePlan" to cusBasePlanValue,
                "cusChipID" to cusChipIDValue,
                "cusInstallationDate" to cusInstallationDateValue,
                "cusPlanStartDate" to cusPlanStartDateValue,
                "cusAddOn1" to cusAddOn1Value,
                "cusAddOn2" to cusAddOn2Value,
                "cusAddOn3" to cusAddOn3Value,
                "cusAddOn4" to cusAddOn4Value
            )

            val cusPayData = hashMapOf(
                "Year" to year,
                "Jan" to jan,
                "Feb" to feb,
                "Mar" to mar,
                "Apr" to apr,
                "May" to may,
                "Jun" to jun,
                "Jul" to jul,
                "Aug" to aug,
                "Sep" to sep,
                "Oct" to oct,
                "Nov" to nov,
                "Dec" to dec
            )

            // Save customer data to Firestore
            firestore.collection("admins").document(adminEmail).collection("customers").document(custCFFValue).set(cusPData)
                .addOnSuccessListener {
                    // Data saved successfully
                    firestore.collection("admins").document(adminEmail).collection("customers").document(custCFFValue).collection("service").document(cusChipIDValue).set(cusSData)
                        .addOnSuccessListener {
                            // Service data saved successfully
                            firestore.collection("admins").document(adminEmail).collection("customers").document(custCFFValue).collection("payment").document(year.toString()).set(cusPayData)
                                .addOnSuccessListener {
                                    // Payment data saved successfully
                                    callback(true)
                                }
                                .addOnFailureListener {
                                    callback(false) // Failed to save payment data
                                }
                        }
                        .addOnFailureListener {
                            callback(false) // Failed to save service data
                        }
                }
                .addOnFailureListener {
                    callback(false) // Failed to save customer data
                }
        }
    }

    fun showCustomAlert(context: Context, status: String, errorMessage: String, icon: Int) {
        val myCustomAlert = customAlert(context)
        myCustomAlert.setData(status, icon, errorMessage)
        val customAlertView = myCustomAlert.getView()
        val alert = AlertDialog.Builder(context)
            .setView(customAlertView)
            .create()
        val okBtn: Button = myCustomAlert.getOkButton()
        alert.show()
        okBtn.setOnClickListener {
            if (!addcus){
                alert.dismiss()
            }
            else{
                alert.dismiss()
                val intent = Intent(this, adminHome::class.java)
                startActivity(intent)
                this.finishAffinity()
            }
        }
    }
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.addCusContainer)

        // Handle back button press based on the current fragment
        if (currentFragment is customerPersonalInfo) { // Replace YourInitialFragment with the name of your initial fragment
            // Handle back button press when the initial fragment is shown
            super.onBackPressed()
        } else {
            serviceimg.setImageResource(R.drawable.notcompleted)
            serviceimgline.setImageResource(R.drawable.line)
            fragmentManager.popBackStack()
        }
    }
    }