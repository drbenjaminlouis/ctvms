package com.example.ctvms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class adminChangePassword : AppCompatActivity() {
    private lateinit var cPassDetails: AdminCPassDetails
    private  lateinit var cpassOTP: AdminCPOTP
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_change_password)

        // Create an instance of your fragment
        val passdetails = AdminCPassDetails()
        val passOTP = AdminCPOTP()

        val cPassDetails = AdminCPassDetails()

        // Get the supportFragmentManager and begin a transaction
        supportFragmentManager.beginTransaction().apply {
            // Replace the contents of the FragmentContainerView with your fragment
            replace(R.id.adminCPView, passdetails)
            // Add the transaction to the back stack (optional)
            // Commit the transaction
            commit()
        }
    }
}
