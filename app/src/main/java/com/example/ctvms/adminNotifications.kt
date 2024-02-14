package com.example.ctvms

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ctvms.R
import com.example.ctvms.databinding.ActivityAdminNotificationsBinding

class adminNotifications : AppCompatActivity() {
    private lateinit var binding: ActivityAdminNotificationsBinding
    private var isComplaintsOn = false
    private var isPlansOn = false
    private var ispaymentsOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set initial state of the toggles
        updateToggleImage()

        // Set OnClickListener for the toggle ImageViews
        binding.complaintstoggle.setOnClickListener { toggleComplaintsSwitch() }
        binding.planstoggle.setOnClickListener { togglePlansSwitch() }
        binding.paymentstoggle.setOnClickListener { togglePaymentsSwitch() }
    }

    private fun toggleComplaintsSwitch() {
        isComplaintsOn = !isComplaintsOn
        updateToggleImage()
        // Perform any actions based on the switch state
    }

    private fun togglePlansSwitch() {
        isPlansOn = !isPlansOn
        updateToggleImage()
        // Perform any actions based on the switch state
    }

    private fun togglePaymentsSwitch() {
        ispaymentsOn = !ispaymentsOn
        updateToggleImage()
        // Perform any actions based on the switch state
    }

    private fun updateToggleImage() {
        val complaintsImageResource = if (isComplaintsOn) R.drawable.toggleon else R.drawable.toggleoff
        binding.complaintstoggle.setImageResource(complaintsImageResource)

        val plansImageResource = if (isPlansOn) R.drawable.toggleon else R.drawable.toggleoff
        binding.planstoggle.setImageResource(plansImageResource)

        val paymentsImageResource = if (ispaymentsOn) R.drawable.toggleon else R.drawable.toggleoff
        binding.paymentstoggle.setImageResource(paymentsImageResource)
    }
}
