package com.example.ctvms.Fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.ctvms.AdminLogin
import com.example.ctvms.R
import com.example.ctvms.aboutApp
import com.example.ctvms.adminChangePassword
import com.example.ctvms.adminNotifications
import com.example.ctvms.databinding.FragmentSettingsBinding
import com.example.ctvms.helpCenter
import com.example.ctvms.personalInfoAdmin
import com.google.firebase.auth.FirebaseAuth

class settingsFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = requireContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        val pesonalinfo = binding.personalinfotext
        val notificationBtn = binding.notificationText
        val changePassword = binding.passwordtext
        val logoutbtn = binding.logoutBtn
        val aboutBtn = binding.aboutLabel
        val helpBtn = binding.helptext
        val emailAddress = "abyjose377@gmail.com"
        val subject = "Support For CTVMS"
        val mailButton = binding.contactText
        val feedbackBtn = binding.feedbackLabel

        pesonalinfo.setOnClickListener {
            val intent = Intent(requireContext(), personalInfoAdmin::class.java)
            startActivity(intent)
        }

        notificationBtn.setOnClickListener(){
            val intent = Intent(requireContext(), adminNotifications::class.java)
            startActivity(intent)
        }

        changePassword.setOnClickListener(){
            val intent = Intent(requireContext(),adminChangePassword::class.java)
            startActivity(intent)
        }

        aboutBtn.setOnClickListener(){
            val intent = Intent(requireContext(),aboutApp::class.java)
            startActivity(intent)
        }

            helpBtn.setOnClickListener(){
                val intent = Intent(requireContext(),helpCenter::class.java)
                startActivity(intent)
            }

        logoutbtn.setOnClickListener(){
            customDialog()
        }

        mailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$emailAddress?subject=${Uri.encode(subject)}")
            startActivity(intent)
        }
        feedbackBtn.setOnClickListener {
            val url = "https://forms.gle/QTvq94BTTz4Q68xB7"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        return binding.root
    }
    private fun customDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.customdialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val yesBtn: Button = dialog.findViewById(R.id.yesBtn)
        val noBtn: Button = dialog.findViewById(R.id.noBtn)

        yesBtn.setOnClickListener(){
            dialog.dismiss()
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", false)
                apply()
            }
            val intent = Intent(requireContext(), AdminLogin::class.java)
            startActivity(intent)
        }
        noBtn.setOnClickListener(){
            dialog.dismiss()
        }
        dialog.show()
    }
}
