package com.example.ctvms

import EmailSender
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import customAlert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ForgotPassword : AppCompatActivity(), forgotOTP.OnVerifyButtonClickListener, adminForgotChange_password.OnChangeButtonClickListener {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var otptext: TextView
    private lateinit var otplayout: ConstraintLayout
    private lateinit var otp : String
    private lateinit var forgotBtn: Button
    private lateinit var email: String
    private var flag:Boolean = false
    private var enableflag:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        forgotBtn = findViewById<Button>(R.id.forgotOTPBtn)
        val forgotemail = findViewById<TextInputEditText>(R.id.adminForgotEmail)
        otplayout = findViewById(R.id.resendOTPLayout)
        otptext = findViewById(R.id.counterText)
        otplayout.visibility = GONE

        forgotBtn.setOnClickListener(){
            email = forgotemail.text.toString()
            val otpSender = EmailSender()
            if (email.isEmpty()) {
                showCustomAlert(this,"Error","Email Should Not Be Empty",R.drawable.errorinfo)
            } else{
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showCustomAlert(this,"Error","Invalid Email Format",R.drawable.errorinfo)
                } else {
                    otp = generateOtp()
                    CoroutineScope(Dispatchers.Main).launch {
                        val sendForgotOTP = otpSender.sendForgotOTP(this@ForgotPassword, email, "Admin", otp)
                        if (sendForgotOTP) {
                            showCustomAlert(this@ForgotPassword,"Success","OTP Sent Successfully",R.drawable.success)
                            val fragContainerView = findViewById<FragmentContainerView>(R.id.forgototpfragment) // Access view directly
                            val fragmentTransaction = supportFragmentManager.beginTransaction() // Use supportFragmentManager
                            val otpFragment = forgotOTP()
                            otpFragment.setOnVerifyButtonClickListener(this@ForgotPassword)
                            fragmentTransaction.replace(fragContainerView.id, otpFragment)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                            forgotBtn?.isEnabled = false
                            val disabledColor = ColorStateList.valueOf(Color.GRAY)
                            forgotBtn?.backgroundTintList = disabledColor
                            otplayout.visibility = View.VISIBLE // Show the timer TextView

                            // Start a coroutine to update the timer and enable the button after 60 seconds
                            val startTime = System.currentTimeMillis()
                            val endTime = startTime + 60000 // 60 seconds in milliseconds
                            var remainingTime = endTime - System.currentTimeMillis()

                            while (remainingTime > 0) {
                                val seconds = (remainingTime / 1000).toInt()
                                val minutes = seconds / 60
                                val secondsDisplay = seconds % 60
                                val timerText = String.format("%02d:%02d", minutes, secondsDisplay)
                                otptext.text = timerText

                                // Delay for 1 second
                                delay(1000)

                                // Update the remaining time
                                remainingTime = endTime - System.currentTimeMillis()
                            }

                            if (!enableflag){
                                forgotBtn.isEnabled = true
                                val enabledColor = ContextCompat.getColorStateList(this@ForgotPassword, R.color.default_button_color)
                                forgotBtn.backgroundTintList = enabledColor
                                otplayout.visibility = View.GONE //
                            }
                        } else {
                            showCustomAlert(this@ForgotPassword,"Error","Something Went Wrong. Try Again",R.drawable.error)
                        }
                    }
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
            if(!flag){
                alert.dismiss()
            }else{
                alert.dismiss()
                val intent = Intent(this, AdminLogin::class.java)
                startActivity(intent)
                this.finishAffinity()
            }
        }
    }

    override fun onVerifyButtonClicked() {
        val adminFOTP = (supportFragmentManager.findFragmentById(R.id.forgototpfragment) as forgotOTP).getFOTPData()
        if (adminFOTP != null) {
            val val1 = adminFOTP.Value1
            val val2 = adminFOTP.Value2
            val val3 = adminFOTP.Value3
            val val4 = adminFOTP.Value4
            val val5 = adminFOTP.Value5
            val val6 = adminFOTP.Value6
            val otpval = "$val1$val2$val3$val4$val5$val6"
            if (otp == otpval) {
                showCustomAlert(this, "Success", "OTP Verification Successful", R.drawable.success)
                enableflag=true
                forgotBtn.isEnabled = false
                val disabledColor = ColorStateList.valueOf(Color.GRAY)
                forgotBtn.backgroundTintList = disabledColor
                otplayout.visibility = View.GONE // Show the timer TextView
                val fragContainerView = findViewById<FragmentContainerView>(R.id.forgototpfragment) // Access view directly
                val fragmentTransaction = supportFragmentManager.beginTransaction() // Use supportFragmentManager
                val passFragment = adminForgotChange_password()
                passFragment.setOnChangeButtonClickListener(this)
                fragmentTransaction.replace(fragContainerView.id, passFragment)
                fragmentTransaction.commit()

            } else {
                showCustomAlert(this, "Error", "Incorrect OTP. Please try again.", R.drawable.errorinfo)
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

    override fun onChangeButtonClicked() {
        val adminFNewPass =
            (supportFragmentManager.findFragmentById(R.id.forgototpfragment) as adminForgotChange_password).getNewPassData()
        if (adminFNewPass != null) {
            val newpass = adminFNewPass.newPass
            // Call the saveAdminPassword function with the adminEmail, newPassword, and a callback function
            saveAdminPassword(this, email, newpass) { success ->
                if (success) {
                    flag=true
                    println("Password updated successfully")
                    showCustomAlert(this,"Success","Password Updated Successfully",R.drawable.success)
                } else {
                    println("Failed to update password")
                }
            }
        }
    }
    fun saveAdminPassword(context: Context, adminEmail: String, newPassword: String, callback: (Boolean) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        // Check if the document with the given admin email exists
        firestore.collection("admins")
            .document(adminEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Admin document exists, proceed with updating the password
                    val adminData = hashMapOf<String, Any>(
                        "adminPassword" to newPassword
                    )
                    firestore.collection("admins")
                        .document(adminEmail) // Use the admin email as the document ID
                        .set(adminData, SetOptions.merge()) // Update only the AdminPassword field
                        .addOnSuccessListener {
                            callback(true) // Data saved successfully
                        }
                        .addOnFailureListener {
                            callback(false) // Failed to save data
                        }
                } else {
                    // Admin document does not exist
                    showCustomAlert(context, "Error", "No Admin Found With Given Email", R.drawable.error)
                    callback(false) // Indicate failure
                }
            }
            .addOnFailureListener { exception ->
                // Error occurred while checking document existence
                println("Error checking admin document: $exception")
                showCustomAlert(context, "Error", "Something went wrong. Please try again later", R.drawable.error)
                callback(false) // Indicate failure
            }
    }
}