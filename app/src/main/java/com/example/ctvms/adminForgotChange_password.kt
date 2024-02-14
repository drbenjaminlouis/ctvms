package com.example.ctvms

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import customAlert


class adminForgotChange_password : Fragment() {
    private lateinit var newpass:TextInputEditText
    private lateinit var connewpass: TextInputEditText
    interface OnChangeButtonClickListener {
        fun onChangeButtonClicked()
    }
    private var buttonClickListener: OnChangeButtonClickListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_forgot_change_password, container, false)
        val ChangeBtn = view.findViewById<Button>(R.id.changePassBtn)
        newpass = view.findViewById(R.id.Newpassword)
        connewpass = view.findViewById(R.id.NewConfirmPassword)
        ChangeBtn.setOnClickListener {
            buttonClickListener?.onChangeButtonClicked()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ChangeBtn = view.findViewById<Button>(R.id.changePassBtn)
        ChangeBtn.setOnClickListener {
            buttonClickListener?.onChangeButtonClicked()
        }
    }
    data class FNewPassData(val newPass: String)
    fun getNewPassData(): FNewPassData? {
        val newpassword = newpass.text.toString()
        val connpassword = connewpass.text.toString()
        if (newpassword.isEmpty()) {
            showCustomAlert("Please Enter Your Password")
            return null
        }
        if (connpassword.isEmpty()) {
            showCustomAlert("Please Confirm Your Password")
            return null
        }
        if (newpassword!=connpassword){
            showCustomAlert("Passwords Are Not Matching")
            return null
        }
        return FNewPassData(newpassword)
    }
    fun setOnChangeButtonClickListener(listener: OnChangeButtonClickListener) {
        this.buttonClickListener = listener
    }
    fun showCustomAlert(errorMessage: String) {
        val myCustomAlert = customAlert(requireContext())
        myCustomAlert.setData("Please Fill In All Details", R.drawable.errorinfo, errorMessage)
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