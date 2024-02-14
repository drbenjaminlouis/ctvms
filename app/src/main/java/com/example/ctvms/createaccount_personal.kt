package com.example.ctvms


import AdminPersonalInfoFragment
import EmailSender
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import customAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class createaccount_personal : AppCompatActivity(), AdminPersonalInfoFragment.OnButtonClickListener,setPassword.OnOTPBtnClickListener, createaccountOTP.OnVerifyButtonClickListener {
    private lateinit var adminID: String
    val firestore = FirebaseFirestore.getInstance()
    private lateinit var adminName: String
    private lateinit var adminEmail: String
    private lateinit var adminPassword: String
    private lateinit var adminMobile: String
    private lateinit var adminGender: String
    private lateinit var adminDob: String
    private lateinit var adminAddress: String
    private lateinit var adminDistrict: String
    private lateinit var adminState: String
    private lateinit var dbRef: DatabaseReference
    private lateinit var otp: String
    private var flag:Boolean = false
    private var signup:Boolean = false
    var lastAdminId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createaccount_personal)

        val adminPersonalInfoFragment = AdminPersonalInfoFragment()

        // Set the button click listener
        adminPersonalInfoFragment.setOnButtonClickListener(this)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragContainer, adminPersonalInfoFragment)
            commit()
        }
    }


    override fun onButtonClicked() {
        // This method will be called when the button in the fragment is clicked
        // You can retrieve the admin name here
        val adminPersonalData = (supportFragmentManager.findFragmentById(R.id.fragContainer) as AdminPersonalInfoFragment).getAdminPersonalData()

        if (adminPersonalData != null) {
            adminName = adminPersonalData.adminName
            adminEmail = adminPersonalData.adminEmail
            adminMobile = adminPersonalData.adminMobile
            adminGender = adminPersonalData.adminGender
            adminDob = adminPersonalData.adminDOB
            adminAddress = adminPersonalData.adminAddress
            adminState = adminPersonalData.adminState
            adminDistrict = adminPersonalData.adminDistrict

            // Create a new instance of setPassword fragment
            val adminSetPasswordFragment = setPassword()

            // Set the button click listener
            adminSetPasswordFragment.setOnOTPButtonClickListener(this)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragContainer, adminSetPasswordFragment)
                addToBackStack(null)
                commit()
                val perchecker = findViewById<ImageView>(R.id.personalChecker)
                val perLine = findViewById<ImageView>(R.id.personalLine)
                perchecker.setImageResource(R.drawable.success)
                perLine.setImageResource(R.drawable.greenline)
            }

        } else {

        }
    }
    override fun onOTPButtonClicked() {
        val adminPassData = (supportFragmentManager.findFragmentById(R.id.fragContainer) as setPassword).getAdminPassData()

        // Check if adminPassData is not null before accessing its properties
        if (adminPassData != null) {
            adminPassword = adminPassData.adminPass
            val OTPTextView = adminPassData.OTPText
            val OTPLayout = adminPassData.otpLayout
            val otpSender = EmailSender()
            val email = adminEmail
            val name = adminName
            otp = generateOtp()
            val adminOTPFrag = createaccountOTP()
            adminOTPFrag.setOnVerifyButtonClickListener(this)

            val otpfrag = supportFragmentManager.findFragmentById(R.id.fragContainer) as setPassword
            val button = otpfrag.view?.findViewById<Button>(R.id.sendOTP)

            lifecycleScope.launch {
                val success = otpSender.sendOTP(this@createaccount_personal, email, name, otp)
                if (success) {
                    val passchecker = findViewById<ImageView>(R.id.passwordChecker)
                    val passLine = findViewById<ImageView>(R.id.passwordLine)
                    passchecker.setImageResource(R.drawable.success)
                    passLine.setImageResource(R.drawable.greenline)
                    // OTP sent successfully
                    showCustomAlert(this@createaccount_personal, "Success", "OTP Sent Successfully", R.drawable.success)

                    // Replace the fragment immediately after the OTP is successfully sent
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.otpFragment, adminOTPFrag)
                        commit()
                    }

                    // Disable the button
                    button?.isEnabled = false
                    val disabledColor = ColorStateList.valueOf(Color.GRAY)
                    button?.backgroundTintList = disabledColor
                    OTPLayout.visibility = View.VISIBLE // Show the timer TextView

                    // Start a coroutine to update the timer and enable the button after 60 seconds
                    val startTime = System.currentTimeMillis()
                    val endTime = startTime + 60000 // 60 seconds in milliseconds
                    var remainingTime = endTime - System.currentTimeMillis()

                    while (remainingTime > 0) {
                        val seconds = (remainingTime / 1000).toInt()
                        val minutes = seconds / 60
                        val secondsDisplay = seconds % 60
                        val timerText = String.format("%02d:%02d", minutes, secondsDisplay)
                        OTPTextView.text = timerText

                        // Delay for 1 second
                        delay(1000)

                        // Update the remaining time
                        remainingTime = endTime - System.currentTimeMillis()
                    }

                    // Enable the button after 60 seconds
                    button?.isEnabled = true
                    val enabledColor = ContextCompat.getColorStateList(this@createaccount_personal, R.color.default_button_color)
                    button?.backgroundTintList = enabledColor
                    OTPLayout.visibility = View.GONE // Hide the timer TextView
                } else {
                    // Failed to send OTP
                    showCustomAlert(this@createaccount_personal, "Error", "Failed to send OTP", R.drawable.error)
                }
            }
        }
    }
    override fun onVerifyButtonClicked() {

        if (!flag) {
            // Find the OTP fragment by ID
            val adminOTP = (supportFragmentManager.findFragmentById(R.id.otpFragment) as createaccountOTP).getOTPData()

            // Check if adminOTPFragment is not null before accessing its data
            if (adminOTP != null) {
                val val1 = adminOTP.Value1
                val val2 = adminOTP.Value2
                val val3 = adminOTP.Value3
                val val4 = adminOTP.Value4
                val val5 = adminOTP.Value5
                val val6 = adminOTP.Value6
                val otpval = "$val1$val2$val3$val4$val5$val6"
                Toast.makeText(this, otp, Toast.LENGTH_SHORT).show()
                if (otp == otpval) {
                    showCustomAlert(
                        this,
                        "Success",
                        "OTP Verification Successfull",
                        R.drawable.success
                    )
                    val verifychecker = findViewById<ImageView>(R.id.verificationChecker)
                    verifychecker.setImageResource(R.drawable.success)
                    flag = true
                    val otpfrag =
                        (supportFragmentManager.findFragmentById(R.id.otpFragment) as createaccountOTP)
                    val buttonid = otpfrag.view?.findViewById<Button>(R.id.VerifyBtn)
                    buttonid?.text = "Sign Up"
                } else {
                    showCustomAlert(this, "Error", "OTP Is Not Valid", R.drawable.errorinfo)
                }
            }
        }else{
            signup = true
            dbRef = FirebaseDatabase.getInstance().getReference("admins")
            saveAdminData { success ->
                if (success) {
                    showCustomAlert(this, "Success", "Account Created Successfully", R.drawable.success)
                } else {
                    showCustomAlert(this, "Error", "Something Went Wrong. Try Again Later", R.drawable.error)
                }
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
            if (!signup){
                alert.dismiss()
            }
            else{
                alert.dismiss()
                val intent = Intent(this, AdminLogin::class.java)
                startActivity(intent)
                this.finishAffinity()
            }
        }
    }
    fun generateOtp(): String {
        val otp = StringBuilder()
        repeat(6) {
            otp.append(Random.nextInt(10))
        }
        return otp.toString()
    }
    fun saveAdminData(callback: (Boolean) -> Unit) {
        val adminData = hashMapOf(
            "adminName" to adminName,
            "adminEmail" to adminEmail,
            "adminPassword" to adminPassword,
            "adminGender" to adminGender,
            "adminDob" to adminDob,
            "adminAddress" to adminAddress,
            "adminDistrict" to adminDistrict,
            "adminState" to adminState,
            "adminMobile" to adminMobile
        )

        firestore.collection("admins")
            .document(adminEmail) // Use the admin email as the document ID
            .set(adminData)
            .addOnSuccessListener {
                callback(true) // Data saved successfully
            }
            .addOnFailureListener {
                callback(false) // Failed to save data
            }
    }
    fun generateAdminId(): String {
        lastAdminId++
        val paddedId = lastAdminId.toString().padStart(4, '0') // Pad the ID with zeros to ensure it's 4 digits long
        return "ctvms$paddedId"
    }
}