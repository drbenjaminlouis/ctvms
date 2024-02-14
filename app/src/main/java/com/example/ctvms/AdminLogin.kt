package com.example.ctvms


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import customAlert

class AdminLogin : AppCompatActivity() {
    private var loginSuccess:Boolean = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        FirebaseApp.initializeApp(this) // Initialize Firebase

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        var isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, adminHome::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back
        }

        val create = findViewById<TextView>(R.id.createText)
        val forgot = findViewById<TextView>(R.id.forgotText)
        val loginbtn = findViewById<Button>(R.id.loginBtn)

        create.setOnClickListener() {
            val intent = Intent(this, createaccount_personal::class.java)
            startActivity(intent)
        }

        forgot.setOnClickListener() {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        loginbtn.setOnClickListener() {
            val email = findViewById<TextInputEditText>(R.id.email)
            val password = findViewById<TextInputEditText>(R.id.password)
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                if (emailText.isEmpty()){
                    showCustomAlert(this,"Error","Email Should Not Be Empty",R.drawable.errorinfo)
                }else{
                    if (passwordText.isEmpty()){
                        showCustomAlert(this,"Error","Password Should Not Be Empty",R.drawable.errorinfo)
                    }
                }
            } else {
                // Check login credentials against Firestore
                val db = FirebaseFirestore.getInstance()
                val adminRef = db.collection("admins").document(emailText)
                adminRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            val adminData = document.data
                            val adminPassword = adminData?.get("adminPassword") as? String
                            if (passwordText == adminPassword) {
                                // Password matches, log in successful
                                Log.d("AdminLogin", "signInWithEmail:success")
                                loginSuccess =true
                                showCustomAlert(this,"Success","Admin Authenticated Successfully",R.drawable.success)
                            } else {
                                // Password does not match
                                showCustomAlert(this,"Error","Given Password Is Incorrect",R.drawable.errorinfo)
                            }
                        } else {
                            // Admin document does not exist
                            showCustomAlert(this,"Error","Admin With Given Email Not Found",R.drawable.errorinfo)
                        }
                    } else {
                        // Error retrieving admin document
                        Log.d("AdminLogin", "Error getting admin document: ", task.exception)
                        showCustomAlert(this,"Error","Something Went Wrong. Try Again Later",R.drawable.error)
                    }
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
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
            if (!loginSuccess){
                alert.dismiss()
            }
            else{
                alert.dismiss()
                with(sharedPreferences.edit()) {
                    putBoolean("isLoggedIn", true)
                    apply()
                }
                val intent = Intent(this, adminHome::class.java)
                startActivity(intent)
                finish() // Finish the current activity to prevent going back
            }
        }
    }
}
