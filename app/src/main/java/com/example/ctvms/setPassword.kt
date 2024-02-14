package com.example.ctvms

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import customAlert

class setPassword : Fragment() {
    private var buttonClickListener: OnOTPBtnClickListener? = null
    private lateinit var adminPassword: TextInputEditText
    private lateinit var adminCPassword: TextInputEditText
    private lateinit var otptext: TextView
    private lateinit var otplayout: ConstraintLayout
    interface OnOTPBtnClickListener {
        fun onOTPButtonClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_set_password, container, false)
        adminPassword = view.findViewById(R.id.Npassword)
        adminCPassword = view.findViewById(R.id.Nconfirmpassword)
        otptext = view.findViewById(R.id.counterText)
        otplayout = view.findViewById(R.id.resendlayout)
        val otpBtn = view.findViewById<Button>(R.id.sendOTP)
        otplayout.visibility = View.GONE
        otpBtn.setOnClickListener {
            buttonClickListener?.onOTPButtonClicked()
        }

        return view
    }

    fun setOnOTPButtonClickListener(listener: OnOTPBtnClickListener) {
        this.buttonClickListener = listener
    }

    data class AdminPassData(val adminPass: String, val OTPText: TextView, val otpLayout: ConstraintLayout)

    fun getAdminPassData(): AdminPassData? {
        val pass = adminPassword.text.toString()
        val cpass = adminCPassword.text.toString()
        val OTPText = otptext
        val OTPLayout = otplayout
        if (pass.isEmpty()){
            showCustomAlert("Please Enter Your Password")
            return null
        }else{
            if (cpass.isEmpty()){
                showCustomAlert("Please Confirm Your Password")
                return null
            }else{
                if (cpass != pass){
                    showCustomAlert("Password Not Matching")
                    return null
                }else{
                    return AdminPassData(pass,OTPText,otplayout)
                }
            }
        }
    }
    fun showCustomAlert(errorMessage: String) {
        val myCustomAlert = customAlert(requireContext())
        myCustomAlert.setData("Error", R.drawable.errorinfo, errorMessage)
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
