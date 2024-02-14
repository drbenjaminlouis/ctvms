package com.example.ctvms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ctvms.databinding.ActivityPersonalInfoAdminBinding

class personalInfoAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalInfoAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInfoAdminBinding.inflate(layoutInflater)
        val backbtn = binding.backBtn
        backbtn.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }
}
