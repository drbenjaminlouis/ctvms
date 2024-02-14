package com.example.ctvms

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class helpCenter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_center)

        val phoneNumber = "6282522127"
        val emailAddress = "abyjose377@gmail.com"
        val subject = "Support For CTVMS"
        val callButton = findViewById<Button>(R.id.callBtn)
        val mailButton = findViewById<Button>(R.id.emailBtn)

        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }
        mailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$emailAddress?subject=${Uri.encode(subject)}")
            startActivity(intent)
        }
    }
}